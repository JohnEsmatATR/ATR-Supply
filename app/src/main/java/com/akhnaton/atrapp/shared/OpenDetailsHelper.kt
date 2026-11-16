package com.akhnaton.atrapp.shared

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenDetailsHelper {

    fun openLink(context: Context, offerLink: String?) {
        if (offerLink != null) {
            try {
                val uri = Uri.parse(offerLink)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(intent)

            } catch (e: Exception) {
                val uri = Uri.parse("https://$offerLink")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(intent)
            }
        }
    }

}