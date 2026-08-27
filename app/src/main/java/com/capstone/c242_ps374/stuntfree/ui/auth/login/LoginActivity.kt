package com.capstone.c242_ps374.stuntfree.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityLoginBinding
import com.capstone.c242_ps374.stuntfree.ui.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.auth.register.RegisterActivity
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.api.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.ui.quiz.QuizActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.logoImageView.setImageResource(R.drawable.icon_app)

        binding.btnLogin.setOnClickListener { attemptLogin() }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupObservers() {
        authViewModel.loginStatus.observe(this) { resource ->
            handleLoginStatus(resource)
        }

        authViewModel.navigateToQuiz.observe(this) {
            navigateToActivity(QuizActivity::class.java, true)
        }

        authViewModel.navigateToMain.observe(this) {
            navigateToActivity(MainActivity::class.java, true)
        }

        authViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (!email.isValidEmail()) {
            binding.edLoginEmail.error = getString(R.string.error_invalid_email)
            false
        } else {
            binding.edLoginEmail.error = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.length < 8) {
            binding.edLoginPassword.error = getString(R.string.error_password_length)
            false
        } else {
            binding.edLoginPassword.error = null
            true
        }
    }

    private fun attemptLogin() {
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()

        if (validateEmail(email) && validatePassword(password)) {
            authViewModel.loginUser(email, password)
        }
    }

    private fun handleLoginStatus(resource: Resource<LoginResponse>) {
        when (resource) {
            is Resource.Loading -> {
                binding.progressBar.isVisible = true
                toggleInputs(false)
            }
            is Resource.Success -> {
                binding.progressBar.isVisible = false
                toggleInputs(true)
                resource.data?.message?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                } ?: run {
                    showError(getString(R.string.error_invalid_email))
                }
            }
            is Resource.Error -> {
                binding.progressBar.isVisible = false
                toggleInputs(true)
                showError(resource.message)
            }
        }
    }

    private fun toggleInputs(enabled: Boolean) {
        binding.apply {
            edLoginEmail.isEnabled = enabled
            edLoginPassword.isEnabled = enabled
            btnLogin.isEnabled = enabled
            btnRegister.isEnabled = enabled
        }
    }

    private fun navigateToActivity(
        destination: Class<out AppCompatActivity>,
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(this, destination)
        startActivity(intent)
        if (finishCurrent) finish()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }

    private fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}