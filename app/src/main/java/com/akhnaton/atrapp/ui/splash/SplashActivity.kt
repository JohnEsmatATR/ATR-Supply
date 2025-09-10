package com.akhnaton.atrapp.ui.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivitySplashBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.onBoarding.OnBoardingActivity
import com.akhnaton.atrapp.ui.auth.onBoarding.WelcomeActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    private var logoAnim: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNotificationPermission()
        init()
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }


    private fun init() {
        SharedPreferenceHelper.init(this@SplashActivity)

        val pInfo: PackageInfo =
            baseContext.packageManager.getPackageInfo(baseContext.packageName, 0)

        SharedPreferenceHelper.version = pInfo.versionName

        logoAnim = AnimationUtils.loadAnimation(baseContext, R.anim.logo_anim)
        binding.imLogo.animation = logoAnim

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            Log.d("dvjnkdvndvdv", "init: ${SharedPreferenceHelper.userObj}")
            if (SharedPreferenceHelper.isLogged!!) {
                val intent = Intent(baseContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(baseContext, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}