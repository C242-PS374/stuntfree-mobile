package com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.R

class PregnancyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregnancy)

        val etUmur: EditText = findViewById(R.id.etUmur)
        val spinnerProvinsi: Spinner = findViewById(R.id.spinnerProvinsi)
        val spinnerTerpenuhi: Spinner = findViewById(R.id.spinnerTerpenuhi)
        val spinnerKelayakan: Spinner = findViewById(R.id.spinnerKelayakan)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        val provinsiList = arrayOf("Aceh", "Bali", "Jakarta", "Jawa Barat", "Jawa Tengah", "Jawa Timur")
        val provinsiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinsiList)
        provinsiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvinsi.adapter = provinsiAdapter

        val yesNoList = arrayOf("Ya", "Tidak")
        val yesNoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yesNoList)
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTerpenuhi.adapter = yesNoAdapter
        spinnerKelayakan.adapter = yesNoAdapter

        btnSubmit.setOnClickListener {
            val umur = etUmur.text.toString()
            val provinsi = spinnerProvinsi.selectedItem.toString()
            val terpenuhi = spinnerTerpenuhi.selectedItem.toString()
            val kelayakan = spinnerKelayakan.selectedItem.toString()

            if (umur.isNotEmpty() && provinsi.isNotEmpty() && terpenuhi.isNotEmpty() && kelayakan.isNotEmpty()) {
                Toast.makeText(this, "Data Tersimpan!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
