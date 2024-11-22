package com.capstone.c242_ps374.stuntfree.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.ui.quiz.newborn.NewBornActivity
import com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan.PregnancyActivity

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val btnIbuHamil: Button = findViewById(R.id.optionHamil)
        val btnBalita: Button = findViewById(R.id.optionBaruMelahirkan)

        btnIbuHamil.setOnClickListener {
            val intent = Intent(this, PregnancyActivity::class.java)
            startActivity(intent)
        }

        btnBalita.setOnClickListener {
            val intent = Intent(this, NewBornActivity::class.java)
            startActivity(intent)
        }
    }
}