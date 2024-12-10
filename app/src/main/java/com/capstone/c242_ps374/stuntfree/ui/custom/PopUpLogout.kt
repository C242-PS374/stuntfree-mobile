package com.capstone.c242_ps374.stuntfree.ui.custom

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.capstone.c242_ps374.stuntfree.R
import dagger.hilt.android.AndroidEntryPoint
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import com.capstone.c242_ps374.stuntfree.ui.AuthViewModel

@AndroidEntryPoint
class PopUpLogout : DialogFragment() {

    var onYesClick: (() -> Unit)? = null
    var onNoClick: (() -> Unit)? = null
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.pop_up_logout, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val btnYes = dialogView.findViewById<Button>(R.id.btnLogout)
        val btnNo = dialogView.findViewById<Button>(R.id.btnCancel)

        btnYes.setOnClickListener {
            onYesClick?.invoke()
            dialog.dismiss()
            authViewModel.logout()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }

        btnNo.setOnClickListener {
            onNoClick?.invoke()
            dialog.dismiss()
        }

        return dialog
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
