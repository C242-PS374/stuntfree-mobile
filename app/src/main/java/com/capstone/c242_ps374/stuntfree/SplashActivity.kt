package com.capstone.c242_ps374.stuntfree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            checkSession()
        }, 1500)
    }

    private fun checkSession() {
        lifecycleScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn().first()
            val isFirstTime = sessionManager.isFirstTime().first()
            when {
                isLoggedIn -> {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }
                isFirstTime -> {
                    startActivity(Intent(this@SplashActivity, GetStartedActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            }
            finish()
        }
    }
}