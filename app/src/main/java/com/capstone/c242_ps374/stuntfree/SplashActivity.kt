package com.capstone.c242_ps374.stuntfree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkSession()
        }, 1500)
    }

    private fun checkSession() {
        when {
            sessionManager.isLoggedIn() -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            sessionManager.isFirstTime() -> {
                startActivity(Intent(this, GetStartedActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }
}