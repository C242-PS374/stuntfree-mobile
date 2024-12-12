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
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodItem
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import com.capstone.c242_ps374.stuntfree.databinding.ActivityResultCameraBinding
import com.capstone.c242_ps374.stuntfree.ui.ScanFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ResultCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraBinding
    private val scanFoodViewModel: ScanFoodViewModel by viewModels()

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
        scanFoodViewModel.foodScanResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = resource.data
                    if (response != null) {
                        displayFoodData(response)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val imageUriString = intent.getStringExtra("IMAGE_URI")

        val imageUri = Uri.parse(imageUriString)
        val imageFile = getFileFromUri(imageUri)

        if (imageFile?.exists() == true) {
            scanFoodViewModel.scanFood(imageFile)
        } else {
            Log.e("ResultCameraActivity", "Gagal membuat file dari URI")
            Toast.makeText(this@ResultCameraActivity, "Gagal memproses gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayFoodData(foodScanResponse: FoodScanResponse) {
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