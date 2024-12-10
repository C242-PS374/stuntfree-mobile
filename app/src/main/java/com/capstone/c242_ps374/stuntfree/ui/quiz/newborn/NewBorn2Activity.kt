package com.capstone.c242_ps374.stuntfree.ui.quiz.newborn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.databinding.ActivityNewBorn2Binding
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class NewBorn2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBorn2Binding
    private val newBornViewModel: AttachViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBorn2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        binding.btnSubmit.setOnClickListener {
            attemptSubmit()
        }
    }

    private fun setupObservers() {
        newBornViewModel.infancyProfileResponse.observe(this) { resource ->
            handleSubmissionStatus(resource)
        }
    }

    private fun attemptSubmit() {
        val umurAnak = intent.getStringExtra("TANGGAL_LAHIR") ?: ""
        val tempatTinggal = intent.getStringExtra("TEMPAT_TINGGAL") ?: ""
        val giziTerpenuhi = intent.getStringExtra("GIZI_TERPENUHI") ?: ""
        val kelayakanLingkungan = intent.getStringExtra("KELAYAKAN_LINGKUNGAN") ?: ""

        val tinggiBadan = binding.etHeightBirth.text.toString()
        val beratBadan = binding.etWeightBirth.text.toString()
        val currentHeight = binding.etCurrentHeight.text.toString()
        val currentWeight = binding.etCurrentWeight.text.toString()

        // Ambil gender dari RadioGroup
        val selectedGenderId = binding.radioGender.checkedRadioButtonId
        val genderApiValue = when (selectedGenderId) {
            binding.radioYesGender.id -> "male"
            binding.radioNoGender.id -> "female"
            else -> ""
        }

        // Format tanggal
        val formattedDate = try {
            SimpleDateFormat("d-MM-yyyy", Locale.getDefault()).parse(umurAnak)?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            }
        } catch (e: Exception) {
            null
        }

        if (isValidInput(tinggiBadan, beratBadan, currentHeight, currentWeight) &&
            !formattedDate.isNullOrEmpty() &&
            genderApiValue.isNotEmpty()
        ) {
            val newBornRequest = InfancyRequest(
                childDob = formattedDate,
                childGender = genderApiValue,
                childBornWeight = beratBadan.toInt(),
                childBornHeight = tinggiBadan.toInt(),
                childHeight = currentHeight.toInt(),
                childWeight = currentWeight.toInt(),
                address = tempatTinggal,
                isEnvironmentSuitable = kelayakanLingkungan.equals("Ya", ignoreCase = true),
                isNutritionFulfilled = giziTerpenuhi.equals("Ya", ignoreCase = true)
            )
            newBornViewModel.attachInfancyProfile(newBornRequest)
        } else {
            showError("Harap isi semua data dengan benar!")
        }
    }

    private fun isValidInput(vararg inputs: String): Boolean {
        return inputs.all { it.toIntOrNull() != null && it.toInt() > 0 }
    }

    private fun handleSubmissionStatus(resource: Resource<InfancyResponse>) {
        when (resource) {
            is Resource.Loading -> {
                binding.progressBar.isVisible = true
                toggleInputs(false)
            }
            is Resource.Success -> {
                binding.progressBar.isVisible = false
                toggleInputs(true)
                navigateToMain()
            }
            is Resource.Error -> {
                binding.progressBar.isVisible = false
                toggleInputs(true)
                showError(resource.message)
            }
        }
    }

    private fun toggleInputs(enabled: Boolean) {
        binding.apply {
            etHeightBirth.isEnabled = enabled
            etWeightBirth.isEnabled = enabled
            etCurrentHeight.isEnabled = enabled
            etCurrentWeight.isEnabled = enabled
            radioGender.isEnabled = enabled
            btnSubmit.isEnabled = enabled
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }
}
