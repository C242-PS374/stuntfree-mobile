package com.capstone.c242_ps374.stuntfree.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.databinding.ActivityTodayLogDetailsBinding

class TodayLogDetailsActivity : AppCompatActivity() {

    // Deklarasi binding untuk activity_today_log_details.xml
    private lateinit var binding: ActivityTodayLogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTodayLogDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            // Aksi untuk tombol Save My Food
        }
    }
}
