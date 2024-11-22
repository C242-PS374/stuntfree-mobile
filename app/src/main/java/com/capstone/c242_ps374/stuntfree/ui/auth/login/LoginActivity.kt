package com.capstone.c242_ps374.stuntfree.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.c242_ps374.stuntfree.MainActivity
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ActivityLoginBinding
import com.capstone.c242_ps374.stuntfree.ui.auth.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.auth.password.ForgotPasswordActivity
import com.capstone.c242_ps374.stuntfree.ui.auth.register.RegisterActivity
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.ui.quiz.QuizActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.logoImageView.setImageResource(R.drawable.dicoding_logo)

//        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                validateEmail(s.toString())
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })

//        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                validatePassword(s.toString())
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun setupObservers() {
        authViewModel.loginStatus.observe(this) { resource ->
            handleLoginStatus(resource)
        }

        authViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                navigateToMain()
            }
        }
        Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show()
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
                navigateToMain()
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
            btnForgotPassword.isEnabled = enabled
        }
    }

//    private fun navigateToMain() {
//        Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(this)
//            finish()
//        }
//    }
    private fun navigateToMain() {
        Intent(this, QuizActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }


    private fun showError(message: String?) {
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }

    private fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}