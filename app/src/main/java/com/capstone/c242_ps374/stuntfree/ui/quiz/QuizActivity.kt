package com.capstone.c242_ps374.stuntfree.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityQuizBinding
import com.capstone.c242_ps374.stuntfree.ui.quiz.newborn.NewBornActivity
import com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan.PregnancyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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