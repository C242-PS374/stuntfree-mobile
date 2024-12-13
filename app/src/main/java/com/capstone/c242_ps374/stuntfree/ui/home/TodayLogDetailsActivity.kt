package com.capstone.c242_ps374.stuntfree.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.data.api.get.TodayResponse
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResult
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import com.capstone.c242_ps374.stuntfree.data.model.FoodItem
import com.capstone.c242_ps374.stuntfree.databinding.ActivityTodayLogDetailsBinding
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.ScanFoodViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGropAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayLogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodayLogDetailsBinding
    private val attachesViewModel: AttachViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayLogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachesViewModel.todayLog()

        observeData()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
        }
    }

    private fun observeData() {
        attachesViewModel.todayLogResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = resource.data
                    if (response != null) {
                        Log.d("TodayLogDetailsActivity", "Response: $response")
                        displayFoodScanResult(response)
                        Toast.makeText(this, "Log makanan berhasil diambil", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("TodayLogDetailsActivity", "Response is null")
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("TodayLogDetailsActivity", "Error: ${resource.message}")
                    Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayFoodScanResult(todayResponse: TodayResponse) {
        val foodData = todayResponse.result.map { todayLog ->
            SubmitFoodLogResult(
                id = todayLog.id,
                isAkgFulfilled = todayLog.isAkgFulfilled,
                userId = todayLog.userId,
                imgUrl = todayLog.imgUrl,
                createdAt = todayLog.createdAt,
                foods = todayLog.foods
            )
        }

        val foodGroupAdapter = FoodGropAdapter()
        foodGroupAdapter.submitList(foodData)

        binding.dataGrid.apply {
            layoutManager = LinearLayoutManager(this@TodayLogDetailsActivity)
            adapter = foodGroupAdapter
        }
    }
}