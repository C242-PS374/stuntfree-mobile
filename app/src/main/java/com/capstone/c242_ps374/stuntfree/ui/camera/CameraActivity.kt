package com.capstone.c242_ps374.stuntfree.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog
import com.capstone.c242_ps374.stuntfree.databinding.ActivityCameraBinding
import com.capstone.c242_ps374.stuntfree.ui.ScanFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.SubmitFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var isFlashEnabled = false

    private val submitFoodViewModel: SubmitFoodViewModel by viewModels()
    private val scanFoodViewModel: ScanFoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.takePhotoButton.setOnClickListener { takePhoto() }
        binding.backButton.setOnClickListener { finish() }
        binding.flashButton.setOnClickListener { toggleFlash() }
        binding.uploadButton.setOnClickListener { openGallery() }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let { uri ->
                val intent = Intent(this, ResultCamera2Activity::class.java)
                intent.putExtra("IMAGE_URI", uri.toString())
                resultCameraLauncher.launch(intent)
            }
        }
    }

    private val resultCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Food log submitted successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("CameraActivity", "Missing data from ResultCameraActivity")
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val imageFile1 = File(
            externalMediaDirs.firstOrNull(),
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        cleanUpTemporaryFiles(imageFile1)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile1).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraActivity", "Photo capture failed: ${exc.message}", exc)
                    Toast.makeText(this@CameraActivity, "Photo capture failed", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val photoUri = Uri.fromFile(imageFile1)
                    Toast.makeText(this@CameraActivity, "Photo saved: $photoUri", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@CameraActivity, ResultCameraActivity::class.java)
                    intent.putExtra("IMAGE_URI", photoUri.toString())
                    startActivity(intent)
                }
            }
        )
    }

    private fun cleanUpTemporaryFiles(photoFile: File) {
        if (photoFile.exists()) {
            photoFile.delete()
            Log.d("CameraActivity", "Temporary file deleted: ${photoFile.path}")
        }
    }

    private fun toggleFlash() {
        isFlashEnabled = !isFlashEnabled

        camera?.cameraControl?.enableTorch(isFlashEnabled)

        binding.flashButton.setBackgroundResource(
            if (isFlashEnabled) R.drawable.ic_flash_on else R.drawable.ic_flash
        )

        Toast.makeText(
            this,
            if (isFlashEnabled) "Flash Enabled" else "Flash Disable",
            Toast.LENGTH_SHORT
        ).show()

        Log.d("CameraActivity", "Flash mode: ${if (isFlashEnabled) "ON" else "OFF"}")
    }
}