package com.akhnaton.atrSupply

import android.app.Application
import android.content.Context
import com.akhnaton.atrSupply.shared.LocaleHelper
import com.akhnaton.atrSupply.shared.SharedPreferenceHelper

class AtrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.init(this)
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(base))
    }
}