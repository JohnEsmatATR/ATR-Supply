package com.akhnaton.atrapp

import android.app.Application
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class AtrApp  : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.init(this) // 🟢 مرر الـ context هنا
    }
}