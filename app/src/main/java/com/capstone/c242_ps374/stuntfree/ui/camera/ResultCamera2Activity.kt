package com.capstone.c242_ps374.stuntfree.ui.camera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResponse
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodItem
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import com.capstone.c242_ps374.stuntfree.databinding.ActivityResultCameraBinding
import com.capstone.c242_ps374.stuntfree.ui.ScanFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.SubmitFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.SubmitFoodAdapter
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ResultCamera2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraBinding
    private val scanFoodViewModel: ScanFoodViewModel by viewModels()
    private val submitFoodViewModel: SubmitFoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("IMAGE_URI")

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)

            Glide.with(this)
                .load(imageUri)
                .into(binding.previewImage)
        }

        observeData()

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeData() {
        submitFoodViewModel.submitFood.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = resource.data
                    if (response != null) {
                        displaySubmitFood(response)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Error submitting food log: ${resource.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        scanFoodViewModel.foodScanResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = resource.data
                    // Tambahkan logika untuk menampilkan hasil scan
                    if (response != null) {
                        displayFoodScanResult(response)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            val imageFile = getFileFromUri(imageUri)

            if (imageFile?.exists() == true) {
                // Scan gambar untuk mendapatkan data array dari model
                scanFoodViewModel.scanFood(imageFile)

                // Contoh pemrosesan hasil model (simulasi data)
                scanFoodViewModel.foodScanResult.observe(this) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val foodData = resource.data
                            val submitData = foodData?.let { mapToSubmitFoodLog(it) } ?: emptyList()

                            // Kirim data log makanan
                            submitFoodViewModel.submitFoodLog(
                                imageFile,
                                submitData.toCollection(ArrayList())
                            )
                        }
                        is Resource.Error -> {
                            Log.e("ResultCamera2Activity", "Error: ${resource.message}")
                            Toast.makeText(this, "Gagal memproses model: ${resource.message}", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            // Tampilkan indikator loading (jika diperlukan)
                        }
                    }
                }
            } else {
                Log.e("ResultCamera2Activity", "Gagal membuat file dari URI")
                Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun mapToSubmitFoodLog(submitFoodLogResponse: FoodScanResponse): List<SubmitFoodLog> {
        return submitFoodLogResponse.result.map { foodItem ->
            SubmitFoodLog(
                name = foodItem.name,
                qty = foodItem.qty
            )
        }
    }


    // Tambahkan method untuk menampilkan hasil scan
    private fun displayFoodScanResult(foodScanResponse: FoodScanResponse) {
        // Implementasi sesuai kebutuhan Anda
        binding.foodTitle.text = foodScanResponse.message

        val foodData = foodScanResponse.result.map { foodItem ->
            FoodItem(
                name = foodItem.name,
                qty = foodItem.qty,
                nutrition = foodItem.nutrition
            )
        }

        val foodGroupAdapter = FoodGroupAdapter(foodData)
        binding.dataGrid.apply {
            layoutManager = LinearLayoutManager(this@ResultCamera2Activity)
            adapter = foodGroupAdapter
        }
    }

    private fun displaySubmitFood(submitFoodLogResponse: SubmitFoodLogResponse) {
        binding.foodTitle.text = submitFoodLogResponse.message

        val submitData = submitFoodLogResponse.result.map { submitFoodLog ->
            SubmitFoodLog(
                name = submitFoodLog?.name,
                qty = submitFoodLog?.qty
            )
        }

        val foodGroupAdapter = SubmitFoodAdapter(submitData)
        binding.dataGrid.apply {
            layoutManager = LinearLayoutManager(this@ResultCamera2Activity)
            adapter = foodGroupAdapter
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val tempDir = File(cacheDir, "temp_images")
            tempDir.mkdirs()

            val tempFile = File(tempDir, "selected_image_${System.currentTimeMillis()}.jpg")

            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { fileOut ->
                    inputStream.copyTo(fileOut)
                }
            }

            Log.d("ResultCameraActivity", "File created: ${tempFile.absolutePath}")
            tempFile
        } catch (e: Exception) {
            Log.e("ResultCameraActivity", "Error converting URI to File", e)
            null
        }
    }
}