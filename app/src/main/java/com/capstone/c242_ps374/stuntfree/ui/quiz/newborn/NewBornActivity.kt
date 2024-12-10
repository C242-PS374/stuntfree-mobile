package com.capstone.c242_ps374.stuntfree.ui.quiz.newborn

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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

        setupListeners()
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
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.etUmur.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun attemptSubmit() {
        val umur = binding.etUmur.text.toString()
        val address = binding.etAddress.text.toString()
        val terpenuhi = if (binding.radioNutrition.checkedRadioButtonId == binding.radioYesNutrition.id) "Yes" else "No"
        val kelayakan = if (binding.radioEnvironment.checkedRadioButtonId == binding.radioYesEnvi.id) "Yes" else "No"

        if (isValidInput(umur, address, terpenuhi, kelayakan)) {
            navigateToNextScreen(umur, address, terpenuhi, kelayakan)
        } else {
            showError()
        }
    }

    private fun isValidInput(umur: String, address: String, terpenuhi: String, kelayakan: String): Boolean {
        return umur.isNotEmpty() && address.isNotEmpty() && terpenuhi.isNotEmpty() && kelayakan.isNotEmpty()
    }

    private fun navigateToNextScreen(umur: String, address: String, terpenuhi: String, kelayakan: String) {
        val intent = Intent(this, NewBorn2Activity::class.java).apply {
            putExtra("TANGGAL_LAHIR", umur)
            putExtra("ALAMAT", address)
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
