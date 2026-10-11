package com.akhnaton.atrapp.shared

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.akhnaton.atrapp.R
import kotlin.apply
import kotlin.text.isNullOrBlank

object DialogUtils {

    fun showResultDialog(
        context: Context,
        icon: Int,
        title: String,
        description: String? = null,

        // Buttons
        yesText: String = "Confirm",
        noText: String = "Cancel",
        buttonOrientation: Int = LinearLayout.HORIZONTAL,

        isDismissable: Boolean = false,
        isOkMessage: Boolean = true,

        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null,
    ) {

        val dialog = Dialog(context)

        dialog.setContentView(R.layout.dialog_result)

        dialog.window?.setBackgroundDrawableResource(
            `in`.aabhasjindal.otptextview.R.color.transparent
        )

        dialog.setCanceledOnTouchOutside(isDismissable)
        dialog.setCancelable(isDismissable)

        // Views
        val imgStatus =
            dialog.findViewById<ImageView>(R.id.imgStatus)

        val tvTitle =
            dialog.findViewById<TextView>(R.id.tvTitle)

        val tvDescription =
            dialog.findViewById<TextView>(R.id.tvDescription)

        val buttonsContainer =
            dialog.findViewById<LinearLayout>(R.id.buttonsContainer)

        val btnYes =
            dialog.findViewById<TextView>(R.id.btnYes)

        val btnNo =
            dialog.findViewById<TextView>(R.id.btnNo)

        // Icon
        imgStatus.setImageResource(icon)

        // Title
        tvTitle.text = title

        // Description
        if (description.isNullOrBlank()) {

            tvDescription.visibility = View.GONE

        } else {

            tvDescription.visibility = View.VISIBLE
            tvDescription.text = description
        }

        if (isOkMessage) {
            btnNo.visibility = View.GONE
        }

        // Button texts
        btnYes.text = yesText
        btnNo.text = noText

        // Button orientation
        buttonsContainer.orientation = buttonOrientation

        if (buttonOrientation == LinearLayout.VERTICAL) {

            btnYes.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = context.resources
                    .getDimensionPixelSize(R.dimen.margin_bottom2)
            }

            btnNo.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        } else {

            btnYes.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                marginEnd = context.resources
                    .getDimensionPixelSize(R.dimen.margin_bottom2)
            }

            btnNo.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
            }
        }

        // Confirm
        btnYes.setOnClickListener {

            dialog.dismiss()

            onConfirm?.invoke()
        }

        // Cancel
        btnNo.setOnClickListener {

            dialog.dismiss()

            onCancel?.invoke()
        }

        dialog.show()
    }
}