package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.databinding.FragmentProfileBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderHistoryActivity


class ProfileFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.accountLayout.setOnClickListener(this)
        binding.orderLayout.setOnClickListener(this)
        binding.languagesLayout.setOnClickListener(this)
        binding.privacyLayout.setOnClickListener(this)
        binding.aboutLayout.setOnClickListener(this)
        binding.contactLayout.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        if (v.id == binding.accountLayout.id) {
            val intent = Intent(requireContext(), AccountDetailsActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.orderLayout.id) {
            val intent = Intent(requireContext(), OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.languagesLayout.id) {
            val intent = Intent(requireContext(), LanguageActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.privacyLayout.id) {
            val intent = Intent(requireContext(), PrivacyActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.aboutLayout.id) {
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.contactLayout.id) {
            val intent = Intent(requireContext(), ContactUsActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.logoutLayout.id) {
            SharedPreferenceHelper.let {
                it.isLogged = false
                it.userObj = null
                it.userToken = null
                it.language = "en"
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }


}