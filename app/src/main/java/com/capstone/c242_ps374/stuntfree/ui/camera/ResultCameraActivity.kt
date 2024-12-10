package com.capstone.c242_ps374.stuntfree.ui.camera

import android.net.Uri
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.model.FoodItem
import com.capstone.c242_ps374.stuntfree.databinding.ActivityResultCameraBinding
import com.capstone.c242_ps374.stuntfree.ui.adapter.FoodGroupAdapter

class ResultCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)

            Glide.with(this)
                .load(imageUri)
                .into(binding.previewImage)
        }


        binding.foodTitle.text = "Avocado Sandwich with Sunny Side Up"


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

        val foodGroupAdapter = FoodGroupAdapter(foodData)
        binding.dataGrid.layoutManager = LinearLayoutManager(this)
        binding.dataGrid.adapter = foodGroupAdapter
    }
}