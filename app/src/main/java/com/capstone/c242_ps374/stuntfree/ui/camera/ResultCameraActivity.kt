package com.capstone.c242_ps374.stuntfree.ui.camera

import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityResultCameraBinding

class ResultCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.previewImage.setImageResource(R.drawable.test_image)


        binding.foodTitle.text = "Avocado Sandwich with Sunny Side Up"


        val dummyData = listOf(
            "Calories: 250 kcal",
            "Protein: 10g",
            "Fat: 20g",
            "Carbs: 15g",
            "Fiber: 3g",
            "Sugar: 1g"
        )

        populateGridLayout(dummyData)
    }

    private fun populateGridLayout(data: List<String>) {
        for (item in data) {
            val textView = TextView(this).apply {
                text = item
                setPadding(16)
                setTextColor(resources.getColor(android.R.color.white, null))
                setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
                gravity = android.view.Gravity.CENTER
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            binding.dataGrid.addView(textView, params)
        }
    }
}