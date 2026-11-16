package com.akhnaton.atrapp.ui.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityUpdateBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.OpenDetailsHelper
import kotlinx.coroutines.launch

class UpdateActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUpdateBinding
    private var APP_LINK = "https://play.google.com/store/apps/details?id=com.akhnaton.atrapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = true

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupKeyboardInsets()

        binding.buttonUpdate.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        OpenDetailsHelper().openLink(this,APP_LINK)
    }

    private fun setupKeyboardInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeInsets = insets.getInsets(
                WindowInsetsCompat.Type.ime()
            )
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                maxOf(
                    imeInsets.bottom,
                    systemBars.bottom
                )
            )
            insets
        }
    }

    override fun onResume() {
        super.onResume()

//        if (
//            SharedPreferencesHelper.shouldRestartApp
//        ) {
//            SharedPreferencesHelper.shouldRestartApp = false
//            val intent = Intent(
//                this,
//                SplashActivity::class.java
//            )
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//            return
//        }
    }
}