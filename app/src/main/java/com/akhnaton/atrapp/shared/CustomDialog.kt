package com.akhnaton.atrapp.shared

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.akhnaton.atrapp.R

class CustomDialog(private val context: Context) {
    private lateinit var dialog: AlertDialog

    fun showDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog, null)
        val trackButton = view.findViewById<Button>(R.id.positive_button)
        trackButton.setOnClickListener {
            dismissDialog()
        }
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(view)
        dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun dismissDialog() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}
