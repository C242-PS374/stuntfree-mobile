package com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.databinding.ActivityPregnancyBinding
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PregnancyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPregnancyBinding
    private val attachViewModel: AttachViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPregnancyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        binding.btnSubmit.setOnClickListener { attemptSubmit() }
    }

    private fun setupObservers() {
        attachViewModel.pregnancyProfileResponse.observe(this) { resource ->
            handleSubmissionStatus(resource)
        }
    }

    private fun attemptSubmit() {
        val umur = binding.etUmur.text.toString()
        val address = binding.etAddress.text.toString()
        val isNutritionFulfilled = getSelectedRadioButtonText(binding.radioNutrition)
        val isEnvironmentSuitable = getSelectedRadioButtonText(binding.radioEnvironment)

        if (isValidInput(umur, address, isNutritionFulfilled, isEnvironmentSuitable)) {
            val pregnancyRequest = PregnancyRequest(
                gestasionalAge = umur.toInt(),
                address = address,
                isNutritionFulfilled = isNutritionFulfilled.equals("Yes", ignoreCase = true),
                isEnvironmentSuitable = isEnvironmentSuitable.equals("Yes", ignoreCase = true)
            )
            attachViewModel.attachPregnancyProfile(pregnancyRequest)
        } else {
            showError("Harap isi semua data!")
        }
    }

    private fun isValidInput(
        umur: String,
        address: String,
        isNutritionFulfilled: String?,
        isEnvironmentSuitable: String?
    ): Boolean {
        return umur.isNotEmpty() && address.isNotEmpty() && isNutritionFulfilled != null && isEnvironmentSuitable != null
    }

    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String? {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) findViewById<RadioButton>(selectedId).text.toString() else null
    }

    private fun handleSubmissionStatus(resource: Resource<PregnancyResponse>) {
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
            etUmur.isEnabled = enabled
            etAddress.isEnabled = enabled
            radioNutrition.isEnabled = enabled
            radioEnvironment.isEnabled = enabled
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
