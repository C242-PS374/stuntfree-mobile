package com.capstone.c242_ps374.stuntfree.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.databinding.ActivityQuizBinding
import com.capstone.c242_ps374.stuntfree.ui.quiz.newborn.NewBornActivity
import com.capstone.c242_ps374.stuntfree.ui.quiz.pregnan.PregnancyActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.optionHamil -> {
                    sessionManager.saveStage("pregnancy")
                    binding.progressBar.visibility = View.GONE
                    startActivity(Intent(this, PregnancyActivity::class.java))
                    finish()
                }
                R.id.optionBaruMelahirkan -> {
                    sessionManager.saveStage("infancy")
                    binding.progressBar.visibility = View.GONE
                    startActivity(Intent(this, NewBornActivity::class.java))
                    finish()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Pilih salah satu opsi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
