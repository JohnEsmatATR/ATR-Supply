package com.akhnaton.atrapp.ui.nav

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityHomeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.cart.CartFragment
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteFragment
import com.akhnaton.atrapp.ui.nav.home.HomeFragment
import com.akhnaton.atrapp.ui.nav.profile.ProfileFragment
import com.akhnaton.atrapp.ui.nav.tracking.TrackingFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Locale

class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayoutDirection()
        setupWindowInsets()
        init()
        onClick()
        handleBackPress()
        checkForAppUpdate()
        binding.btnTracking.setOnClickListener {
            setCurrentFragment(TrackingFragment())
            binding.bottomNavigationView.menu.findItem(R.id.home)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
            binding.bottomNavigationView.menu.findItem(R.id.favorite)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            binding.bottomNavigationView.menu.findItem(R.id.cart)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
            binding.bottomNavigationView.menu.findItem(R.id.profile)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)
        }

        askNotificationPermission()
    }

    private fun setupWindowInsets() {
        // Apply top padding to fragment container for status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.flFragment) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        // Apply bottom margin to bottom navigation for system navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = view.layoutParams as? androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            layoutParams?.bottomMargin = systemBars.bottom
            view.layoutParams = layoutParams
            insets
        }

        // Apply bottom margin to floating button for system navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.btnTracking) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val originalMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._35sdp)
            val layoutParams = view.layoutParams as? androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            layoutParams?.bottomMargin = originalMargin + systemBars.bottom
            view.layoutParams = layoutParams
            insets
        }
    }

    private fun init() {
        initNavBottom()
        setupDrawer()
        handleBackPress()
    }


    private fun onClick() {
//        binding.imMenu.setOnClickListener {
//            binding.drawerLayout.openDrawer(GravityCompat.START, true)
//        }
//        binding.navigationView.setNavigationItemSelectedListener(this)
//
//        try {
//            val header = binding.navigationView.getHeaderView(0)
//            val txtName = header.findViewById(R.id.txt_name) as TextView
//            txtName.text = SharedPreferenceHelper.userObj!!.full_name
//        }catch (e:Exception){}

    }

    private fun setupDrawer() {

//        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
//        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
//
//        actionBarDrawerToggle.syncState()
//
//        // to make the Navigation drawer icon always appear on the action bar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun initNavBottom() {
//        setItemSelected(R.id.home)
        binding.bottomNavigationView.menu[2].isEnabled = false


        binding.btnTracking.setOnClickListener {
            setCurrentFragment(TrackingFragment())
            binding.bottomNavigationView.selectedItemId = R.id.nothig


            binding.bottomNavigationView.menu.findItem(R.id.home)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
            binding.bottomNavigationView.menu.findItem(R.id.favorite)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            binding.bottomNavigationView.menu.findItem(R.id.cart)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
            binding.bottomNavigationView.menu.findItem(R.id.profile)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> setItemSelected(R.id.home)
                R.id.favorite -> setItemSelected(R.id.favorite)
                R.id.cart -> setItemSelected(R.id.cart)
                R.id.profile -> setItemSelected(R.id.profile)
                else -> setItemSelected(R.id.home)
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.home
        setItemSelected(R.id.home)
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setItemSelected(item: Int) {
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()
        val favoriteFragment = FavoriteFragment()
        val profileFragment = ProfileFragment()

        binding.btnTracking.setOnClickListener {
            setCurrentFragment(TrackingFragment())
            binding.bottomNavigationView.menu.findItem(R.id.home)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
            binding.bottomNavigationView.menu.findItem(R.id.favorite)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            binding.bottomNavigationView.menu.findItem(R.id.cart)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
            binding.bottomNavigationView.menu.findItem(R.id.profile)
                .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)
            binding.bottomNavigationView.selectedItemId = R.id.nothig

        }

        when (item) {
            R.id.home -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu.findItem(R.id.home)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
                binding.bottomNavigationView.menu.findItem(R.id.favorite)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                binding.bottomNavigationView.menu.findItem(R.id.cart)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
                binding.bottomNavigationView.menu.findItem(R.id.profile)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)
            }

            R.id.cart -> {
                setCurrentFragment(cartFragment)
                binding.bottomNavigationView.menu.findItem(R.id.home)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
                binding.bottomNavigationView.menu.findItem(R.id.favorite)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                binding.bottomNavigationView.menu.findItem(R.id.cart)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
                binding.bottomNavigationView.menu.findItem(R.id.profile)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)

            }

            R.id.favorite -> {
                setCurrentFragment(favoriteFragment)
                binding.bottomNavigationView.menu.findItem(R.id.home)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
                binding.bottomNavigationView.menu.findItem(R.id.favorite)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                binding.bottomNavigationView.menu.findItem(R.id.cart)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
                binding.bottomNavigationView.menu.findItem(R.id.profile)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)

            }

            R.id.profile -> {
                setCurrentFragment(profileFragment)
                binding.bottomNavigationView.menu.findItem(R.id.home)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
                binding.bottomNavigationView.menu.findItem(R.id.favorite)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                binding.bottomNavigationView.menu.findItem(R.id.cart)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
                binding.bottomNavigationView.menu.findItem(R.id.profile)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)


            }

            else -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu.findItem(R.id.home)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_home)
                binding.bottomNavigationView.menu.findItem(R.id.favorite)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                binding.bottomNavigationView.menu.findItem(R.id.cart)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_cart)
                binding.bottomNavigationView.menu.findItem(R.id.profile)
                    .icon = ContextCompat.getDrawable(this, R.drawable.ic_profile)

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.getBooleanExtra("open_cart", false)) {

            setItemSelected(R.id.cart)
            binding.bottomNavigationView.selectedItemId = R.id.cart
        }
    }
    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            val selectedItemId = binding.bottomNavigationView.selectedItemId

            if (selectedItemId != R.id.home) {
                // لو مش في Home → يرجعه للـ Home
                binding.bottomNavigationView.selectedItemId = R.id.home
                setItemSelected(R.id.home)
            } else {
                // لو في Home → لازم ضغطتين
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    AlertDialog.Builder(this@HomeActivity)
                        .setTitle("الخروج")
                        .setMessage("هل تريد الخروج من التطبيق؟")
                        .setPositiveButton("نعم") { _, _ -> finish() }
                        .setNegativeButton("لا", null)
                        .show()
                } else {
                    Toast.makeText(this@HomeActivity, "اضغط مرة أخرى للخروج", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    private fun checkForAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    UPDATE_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {

                finish()
            }
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(baseContext, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                    return@OnCompleteListener
                }
                val token = task.result

                Toast.makeText(baseContext, "token : $token", Toast.LENGTH_SHORT).show()
            })
        } else {
            askNotificationPermission()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(baseContext, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                    return@OnCompleteListener
                }

            })
        }
    }

    private fun setLayoutDirection() {
        val lang = SharedPreferenceHelper.language ?: Locale.getDefault().language

        if (lang == "ar") {
            ViewCompat.setLayoutDirection(binding.bottomNavigationView, ViewCompat.LAYOUT_DIRECTION_RTL)
            reverseBottomNavMenu(true)
        } else {
            ViewCompat.setLayoutDirection(binding.bottomNavigationView, ViewCompat.LAYOUT_DIRECTION_LTR)
            reverseBottomNavMenu(false)
        }
    }

    private fun reverseBottomNavMenu(isRtl: Boolean) {
        val menu = binding.bottomNavigationView.menu
        menu.clear()

        if (isRtl) {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu_ar)
        } else {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu)
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 100
    }

}