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
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodItem
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import com.capstone.c242_ps374.stuntfree.databinding.ActivityResultCameraBinding
import com.capstone.c242_ps374.stuntfree.ui.ScanFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.SubmitFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ResultCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraBinding
    private val scanFoodViewModel: ScanFoodViewModel by viewModels()
    private val submitFoodViewModel: SubmitFoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("IMAGE_URI")

        imageUriString?.let { imageUriString ->
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
        scanFoodViewModel.foodScanResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = resource.data
                    response?.let {
                        displayFoodScanResult(it)
                        prepareAndSubmitFoodLog(it)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        submitFoodViewModel.submitFood.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Berhasil menambahkan log makanan", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal menambahkan log makanan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        processImageUri()
    }

    private fun processImageUri() {
        val imageUriString = intent.getStringExtra("IMAGE_URI")
        Log.d("ResultCameraActivity", "Image URI: $imageUriString")

        imageUriString?.let { uriString ->
            val imageUri = Uri.parse(uriString)
            val imageFile = getFileFromUri(imageUri)

            if (imageFile?.exists() == true) {
                Log.d("ResultCameraActivity", "File path: ${imageFile.absolutePath}")
                scanFoodViewModel.scanFood(imageFile) // Mulai proses pemindaian makanan
            } else {
                Log.e("ResultCameraActivity", "Gagal membuat file dari URI")
            }
        }
    }

    private fun mapToSubmitFoodLog(scanResponse: FoodScanResponse): List<SubmitFoodLog> {
        return scanResponse.result.map { foodItem ->
            SubmitFoodLog(
                id = null,
                name = foodItem.name,
                qty = foodItem.qty
            )
        }
    }

    private fun prepareAndSubmitFoodLog(scanResponse: FoodScanResponse) {
        try {
            // Konversi scanResponse menjadi List<SubmitFoodLog>
            val submitData = mapToSubmitFoodLog(scanResponse)

            // Ambil file gambar dari intent
            val imageUriString = intent.getStringExtra("IMAGE_URI")
            val imageFile = imageUriString?.let { getFileFromUri(Uri.parse(it)) }

            if (imageFile != null && submitData.isNotEmpty()) {
                // Log data yang akan dikirim
                Log.d("ResultCameraActivity", "Preparing to submit: $submitData with file: ${imageFile.name}")

                // Submit data
                submitFoodViewModel.submitFoodLog(imageFile, submitData)
            } else {
                Log.e("ResultCameraActivity", "Image file or food data is empty.")
                Toast.makeText(this, "Data makanan atau gambar tidak valid.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ResultCameraActivity", "Error while preparing to submit food log.", e)
            Toast.makeText(this, "Terjadi kesalahan saat mengirim data.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun displayFoodScanResult(foodScanResponse: FoodScanResponse) {
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
            layoutManager = LinearLayoutManager(this@ResultCameraActivity)
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