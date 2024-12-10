package com.capstone.c242_ps374.stuntfree.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityLogYourSymptomsBinding
import com.capstone.c242_ps374.stuntfree.ui.adapter.RecyclerStatusAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.StatusItem

class LogYourSymptomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogYourSymptomsBinding

    private val statusList = listOf(
        StatusItem(
            title = "Pregnancy - Day 4",
            stuntingDescription = "3 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Not Fulfilled",
            environmentalDescription = "Healthy Environment"
        ),
        StatusItem(
            title = "Pregnancy - Day 5",
            stuntingDescription = "2 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Partially Fulfilled",
            environmentalDescription = "Moderate Environment"
        ),
        StatusItem(
            title = "Pregnancy - Day 6",
            stuntingDescription = "1 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Fulfilled",
            environmentalDescription = "Optimal Environment"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan View Binding
        binding = ActivityLogYourSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Kembali ke aktivitas sebelumnya
        }

        setupRecyclerStatus()
    }

    private fun setupRecyclerStatus() {
        val recyclerStatusAdapter = RecyclerStatusAdapter(statusList)
        binding.rvLogSymptoms.apply {
            layoutManager = LinearLayoutManager(this@LogYourSymptomsActivity)
            adapter = recyclerStatusAdapter
        }
    }
}
