package com.capstone.c242_ps374.stuntfree.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityQuizBinding
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.quiz.newborn.NewBornActivity
import com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan.PregnancyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val quizViewModel: AttachViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.btnSubmit.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            val isPregnancySelected = binding.optionHamil.isChecked
            val isInfancySelected = binding.optionBaruMelahirkan.isChecked

            quizViewModel.onSubmit(isPregnancySelected, isInfancySelected)
        }

        binding.optionHamil.setOnClickListener {
            if (binding.optionHamil.isChecked) {
                binding.optionBaruMelahirkan.isChecked = false
            }
        }

        binding.optionBaruMelahirkan.setOnClickListener {
            if (binding.optionBaruMelahirkan.isChecked) {
                binding.optionHamil.isChecked = false
            }
        }
    }

    private fun setupObservers() {
        quizViewModel.stageLiveData.observe(this) { stage ->
            binding.progressBar.visibility = View.GONE

            when (stage) {
                "pregnancy" -> {
                    startActivity(Intent(this, PregnancyActivity::class.java))
                    finish()
                }

                "infancy" -> {
                    startActivity(Intent(this, NewBornActivity::class.java))
                    finish()
                }

                "no_selection" -> {
                    Toast.makeText(this, "Pilih salah satu opsi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}