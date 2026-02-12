package com.akhnaton.atrapp.shared

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R

object DebugBannerManager {
    fun show(activity: Activity) {
        if (ConstantLinks.isProd()) return
        val root = activity.findViewById<ViewGroup>(android.R.id.content)
        if (root.findViewWithTag<View>("DEBUG_BANNER") != null) return
        val banner = LayoutInflater.from(activity)
            .inflate(R.layout.debug_banner, root, false)
        banner.tag = "DEBUG_BANNER"
        root.addView(banner)
    }
}
