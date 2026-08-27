package com.capstone.c242_ps374.stuntfree.ui.stuntingreport

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityStuntingReportBinding
import com.capstone.c242_ps374.stuntfree.ui.ArticleViewModel
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StuntingReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStuntingReportBinding

    private val attachViewModel: AttachViewModel by viewModels()
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStuntingReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)

        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        tvSelectedDate.text = currentDate

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            tvSelectedDate.text = sdf.format(selectedDate)
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        observeData()

        attachViewModel.predictStunting()
        articleViewModel.getToday()
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        attachViewModel.predictStuntingResponse.observe(this) { resource ->
            val profile = resource.data
            binding.tvNutriotionIntake.text = "${profile?.result}"
        }

        articleViewModel.todayDate1.observe(this) { resource ->
            binding.tvTitle.text = "Day - $resource"
        }
    }
}
