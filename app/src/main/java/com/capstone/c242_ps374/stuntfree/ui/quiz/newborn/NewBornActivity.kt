package com.capstone.c242_ps374.stuntfree.ui.quiz.newborn

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.databinding.ActivityNewBornBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class NewBornActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBornBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBornBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
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
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener { attemptSubmit() }
        binding.etUmur.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
            binding.etUmur.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun attemptSubmit() {
        val umur = binding.etUmur.text.toString()
        val tinggi = binding.etTinggi.text.toString()
        val berat = binding.etBerat.text.toString()
        val provinsi = binding.spinnerProvinsi.selectedItem.toString()
        val terpenuhi = binding.spinnerTerpenuhi.selectedItem.toString()
        val kelayakan = binding.spinnerKelayakan.selectedItem.toString()

        if (isValidInput(umur, tinggi, berat, provinsi, terpenuhi, kelayakan)) {
            navigateToNextScreen(umur, tinggi, berat, provinsi, terpenuhi, kelayakan)
        } else {
            showError()
        }
    }

    private fun isValidInput(umur: String, tinggi: String, berat: String, provinsi: String, terpenuhi: String, kelayakan: String ): Boolean {
        return umur.isNotEmpty() && tinggi.isNotEmpty() && berat.isNotEmpty() && provinsi.isNotEmpty() && terpenuhi.isNotEmpty() && kelayakan.isNotEmpty()
    }

    private fun navigateToNextScreen(umur: String, tinggi: String, berat: String, provinsi: String, terpenuhi: String, kelayakan: String) {
        val intent = Intent(this, NewBorn2Activity::class.java).apply {
            putExtra("TANGGAL_LAHIR", umur)
            putExtra("TINGGI_BADAN", tinggi)
            putExtra("BERAT_BADAN", berat)
            putExtra("TEMPAT_TINGGAL", provinsi)
            putExtra("GIZI_TERPENUHI", terpenuhi)
            putExtra("KELAYAKAN_LINGKUNGAN", kelayakan)
        }
        startActivity(intent)
        finish()
    }

    private fun showError() {
        Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
    }
}

