package com.capstone.c242_ps374.stuntfree.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.data.model.FoodItem
import com.capstone.c242_ps374.stuntfree.databinding.ActivityTodayLogDetailsBinding
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter

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

        val foodData = listOf(
            FoodItem(
                "Nasi (1)",
                listOf("Energy: 130 kcal", "Protein: 2.6 g", "Total Fat: 0.3 g", "Carbo: 28 g", "Fiber: 0.6 g", "Calcium: 10 mg")
            ),
            FoodItem(
                "Ayam (1)",
                listOf("Energy: 240 kcal", "Protein: 30 g", "Total Fat: 10 g", "Omega 3: 0.1 g", "Omega 6: 1.0 g")
            )
        )

//        val foodGroupAdapter = FoodGroupAdapter(foodData = <nutrient>)
//        binding.dataGrid.layoutManager = LinearLayoutManager(this)
//        binding.dataGrid.adapter = foodGroupAdapter

        binding.saveButton.setOnClickListener {
            // Aksi untuk tombol Save My Food
        }
    }
}
