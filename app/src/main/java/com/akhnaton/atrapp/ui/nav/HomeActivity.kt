package com.akhnaton.atrapp.ui.nav

import android.app.ComponentCaller
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityHomeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.cart.CartFragment
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteFragment
import com.akhnaton.atrapp.ui.nav.home.HomeFragment
import com.akhnaton.atrapp.ui.nav.profile.ProfileFragment
import com.akhnaton.atrapp.ui.nav.tracking.TrackingFragment
import com.akhnaton.atrapp.util.isNetworkAvailable

class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onClick()



    }

    private fun init() {
        initNavBottom()
        setupDrawer()
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

        setItemSelected(R.id.home)
        binding.bottomNavigationView.menu[2].isEnabled = false


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setItemSelected(R.id.home)
                }

                R.id.favorite -> {
                    setItemSelected(R.id.favorite)
                }

                R.id.cart -> {
                    setItemSelected(R.id.cart)
                }

                R.id.profile -> {
                    setItemSelected(R.id.profile)
                }

                else -> {
                    setItemSelected(R.id.home)
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    private fun setItemSelected(item: Int) {
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()
        val favoriteFragment = FavoriteFragment()
        val profileFragment = ProfileFragment()

        binding.btnTracking.setOnClickListener {
            setCurrentFragment(TrackingFragment())
            binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
            binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
            binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
            binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)
            binding.bottomNavigationView.menu[2].isChecked = true

        }

        when (item) {
            R.id.home -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home_fill)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)
            }

            R.id.cart -> {
                setCurrentFragment(cartFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart_fill)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

            }

            R.id.favorite -> {
                setCurrentFragment(favoriteFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite_fill)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

            }

            R.id.profile -> {
                setCurrentFragment(profileFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile_fill)


            }

            else -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home_fill)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

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




}