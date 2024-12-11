package com.capstone.c242_ps374.stuntfree.ui.custom

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.ui.maps.MapsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomDialogRedirectMapsFragment : DialogFragment() {

    var onYesClick: (() -> Unit)? = null
    var onNoClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.item_popup_redirect_to_maps, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val btnYes = dialogView.findViewById<Button>(R.id.btnOpenMaps)
        val btnNo = dialogView.findViewById<Button>(R.id.btnCancel)

        btnYes.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }


        btnNo.setOnClickListener {
            onNoClick?.invoke()
            dialog.dismiss()
        }

        return dialog
    }
}