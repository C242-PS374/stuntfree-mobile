package com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
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

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        val provinsiList = arrayOf(
            "Aceh", "Bali", "Bangka Belitung", "Banten", "Bengkulu", "DI Yogyakarta",
            "DKI Jakarta", "Gorontalo", "Jambi", "Jawa Barat", "Jawa Tengah", "Jawa Timur",
            "Kalimantan Barat", "Kalimantan Selatan", "Kalimantan Tengah", "Kalimantan Timur",
            "Kalimantan Utara", "Kepulauan Riau", "Lampung", "Maluku", "Maluku Utara",
            "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Papua", "Papua Barat", "Riau",
            "Sulawesi Barat", "Sulawesi Selatan", "Sulawesi Tengah", "Sulawesi Tenggara",
            "Sulawesi Utara", "Sumatera Barat", "Sumatera Selatan", "Sumatera Utara", "West Java"
        )
        val provinsiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinsiList)
        provinsiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProvinsi.adapter = provinsiAdapter

        val yesNoList = arrayOf("Ya", "Tidak")
        val yesNoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yesNoList)
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTerpenuhi.adapter = yesNoAdapter
        binding.spinnerKelayakan.adapter = yesNoAdapter

        binding.btnSubmit.setOnClickListener { attemptSubmit() }
    }

    private fun setupObservers() {
        attachViewModel.pregnancyProfileResponse.observe(this) { resource ->
            handleSubmissionStatus(resource)
        }
    }

    private fun attemptSubmit() {
        val umur = binding.etUmur.text.toString().trim()
        val provinsi = binding.spinnerProvinsi.selectedItem.toString()
        val terpenuhi = binding.spinnerTerpenuhi.selectedItem.toString()
        val kelayakan = binding.spinnerKelayakan.selectedItem.toString()

        if (isValidInput(umur, provinsi, terpenuhi, kelayakan)) {
            val pregnancyRequest = PregnancyRequest(
                gestasionalAge = umur.toInt(),
                address = provinsi,
                isEnvironmentSuitable = kelayakan == "Ya",
                isNutritionFulfilled = terpenuhi == "Ya"
            )
            attachViewModel.attachPregnancyProfile(pregnancyRequest)
        } else {
            showError("Harap isi semua data!")
        }
    }

    private fun isValidInput(umur: String, provinsi: String, terpenuhi: String, kelayakan: String): Boolean {
        return umur.isNotEmpty() && provinsi.isNotEmpty() && terpenuhi.isNotEmpty() && kelayakan.isNotEmpty()
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
            spinnerProvinsi.isEnabled = enabled
            spinnerTerpenuhi.isEnabled = enabled
            spinnerKelayakan.isEnabled = enabled
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