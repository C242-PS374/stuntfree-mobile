package com.capstone.c242_ps374.stuntfree.ui.quiz.newborn

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.R

class NewBorn2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_born2)

        val umurAnak = intent.getStringExtra("UMUR_ANAK")
        val tempatTinggal = intent.getStringExtra("TEMPAT_TINGGAL")
        val gizi = intent.getStringExtra("GIZI_TERPENUHI")
        val kelayakanLingkungan = intent.getStringExtra("KELAYAKAN_LINGKUNGAN")

        val jenisKelaminList = arrayOf("Laki-Laki", "Perempuan")
        val jenisKelaminAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminList)
        jenisKelaminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val dropdownJenisKelamin: Spinner = findViewById(R.id.dropdownJenisKelamin)
        dropdownJenisKelamin.adapter = jenisKelaminAdapter

        val etTinggiBadan: EditText = findViewById(R.id.tinggiBadan)
        val etBeratBadan: EditText = findViewById(R.id.beratBadan)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val selectedJenisKelamin = dropdownJenisKelamin.selectedItem.toString()
            val tinggiBadan = etTinggiBadan.text.toString()
            val beratBadan = etBeratBadan.text.toString()

            if (tinggiBadan.isNotEmpty() && beratBadan.isNotEmpty()) {
                Toast.makeText(
                    this,
                    "Data tersimpan:\nUmur: $umurAnak\nTempat Tinggal: $tempatTinggal\nGizi: $gizi, \nKeayakanLingkungan: $kelayakanLingkungan\nJenis Kelamin: $selectedJenisKelamin\nTinggi: $tinggiBadan cm\nBerat: $beratBadan kg",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
