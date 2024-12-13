package com.capstone.c242_ps374.stuntfree.ui.custom

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.capstone.c242_ps374.stuntfree.R

class PopUpEditEnvironment : DialogFragment() {

    var onYesClick: (() -> Unit)? = null
    var onNoClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.pop_up_edit_environment, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val btnYes = dialogView.findViewById<Button>(R.id.btnHealty)
        val btnNo = dialogView.findViewById<Button>(R.id.btnPoor)

        btnYes.setOnClickListener {
            onYesClick?.invoke()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            onNoClick?.invoke()
            dialog.dismiss()
        }

        return dialog
    }
}
