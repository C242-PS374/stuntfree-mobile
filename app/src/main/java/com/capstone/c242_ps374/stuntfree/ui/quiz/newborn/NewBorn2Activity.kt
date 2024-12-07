package com.capstone.c242_ps374.stuntfree.ui.quiz.newborn

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
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

@AndroidEntryPoint
class NewBorn2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBorn2Binding
    private val newBornViewModel: AttachViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBorn2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        val genderList = arrayOf("Laki-Laki", "Perempuan")
        val genderAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderList
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownJenisKelamin.adapter = genderAdapter

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
        val umurAnak = intent.getStringExtra("UMUR_ANAK") ?: ""
        val tinggiBadanAnak = intent.getStringExtra("TINGGI_BADAN") ?: ""
        val beratBadanAnak = intent.getStringExtra("BERAT_BADAN") ?: ""
        val tempatTinggal = intent.getStringExtra("TEMPAT_TINGGAL") ?: ""
        val giziTerpenuhi = intent.getStringExtra("GIZI_TERPENUHI") ?: ""
        val kelayakanLingkungan = intent.getStringExtra("KELAYAKAN_LINGKUNGAN") ?: ""

        val jenisKelamin = binding.dropdownJenisKelamin.selectedItem?.toString() ?: ""
        val tinggiBadan = binding.tinggiBadan.text.toString()
        val beratBadan = binding.beratBadan.text.toString()

        if (isValidInput(tinggiBadan, beratBadan)) {
            val newBornRequest = InfancyRequest(
                childDob = umurAnak,
                childGender = jenisKelamin,
                childBornWeight = beratBadanAnak.toInt(),
                childBornHeight = tinggiBadanAnak.toInt(),
                childHeight = tinggiBadan.toInt(),
                childWeight = beratBadan.toInt(),
                address = tempatTinggal,
                isEnvironmentSuitable = kelayakanLingkungan == "Ya",
                isNutritionFulfilled = giziTerpenuhi == "Ya"
            )

            newBornViewModel.attachInfancyProfile(newBornRequest)
        } else {
            showError("Harap isi semua data dengan benar!")
        }
    }

    private fun isValidInput(tinggiBadan: String, beratBadan: String): Boolean {
        return tinggiBadan.isNotEmpty() && beratBadan.isNotEmpty() &&
                tinggiBadan.toIntOrNull() != null && beratBadan.toIntOrNull() != null
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
            tinggiBadan.isEnabled = enabled
            beratBadan.isEnabled = enabled
            dropdownJenisKelamin.isEnabled = enabled
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