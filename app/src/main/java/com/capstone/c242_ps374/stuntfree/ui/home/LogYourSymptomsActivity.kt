package com.capstone.c242_ps374.stuntfree.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.data.model.LogItem
import com.capstone.c242_ps374.stuntfree.databinding.ActivityLogYourSymptomsBinding
import com.capstone.c242_ps374.stuntfree.ui.adapter.TodaysMealAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.TodaysMealItem
import com.capstone.c242_ps374.stuntfree.ui.camera.CameraActivity
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogFragment
import com.capstone.c242_ps374.stuntfree.ui.custom.PopUpEditEnvironment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogYourSymptomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogYourSymptomsBinding

    private val logItem = LogItem(
        title = "Pregnancy - Day 4",
        stuntingStatusDetail = "3 Days before stunting detection",
        nutritionDetail = "Not Fulfilled",
        environmentalDetail = "Healthy Environment"
    )

    private val statusList = listOf(
        TodaysMealItem(
            title = "Today’s Meal #1",
            description = "1 Chicken, 1 Nasi, and 1 Ikan"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLogYourSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ivEditEnvirontment.setOnClickListener {
            val popUpEditEnvironment = PopUpEditEnvironment()
            popUpEditEnvironment.show(supportFragmentManager, CustomDialogFragment::class.java.simpleName)
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.idAddMore.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }


        binding.tvTitle.text = logItem.title
        binding.tvStuntingStatus.text = logItem.stuntingStatusDetail
        binding.tvNutritionIntake.text = logItem.nutritionDetail
        binding.tvEnvironmentalConditions.text = logItem.environmentalDetail

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = TodaysMealAdapter(statusList)
        binding.rvLogToday.apply {
            layoutManager = LinearLayoutManager(this@LogYourSymptomsActivity)
            this.adapter = adapter
        }
    }
}
