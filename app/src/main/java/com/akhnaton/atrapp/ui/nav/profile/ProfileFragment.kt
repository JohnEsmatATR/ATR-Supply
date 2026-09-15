package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.FragmentProfileBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.nav.tracking.TrackingFragment
import java.util.Locale

class ProfileFragment : BaseFragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var creditLimit: Double = 0.0
    private var usedCredit: Double = 0.0
    private var availableCredit: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        guestHandling()

        binding.accountLayout.setOnClickListener(this)
        binding.orderLayout.setOnClickListener(this)
        binding.languagesLayout.setOnClickListener(this)
        binding.privacyLayout.setOnClickListener(this)
        binding.aboutLayout.setOnClickListener(this)
        binding.contactLayout.setOnClickListener(this)
        binding.logoutLayout.setOnClickListener(this)

        binding.cardMoreCredit.setOnClickListener(this)

        return binding.root
    }

    private fun guestHandling() {
        Log.d("WHAT", SharedPreferenceHelper.isLogged.toString())
        if (SharedPreferenceHelper.isLogged == false) {
            binding.txtUserName.text = resources.getString(R.string.guest)
            binding.accountLayout.visibility = View.GONE
            binding.v1.visibility = View.GONE
            binding.orderLayout.visibility = View.GONE
            binding.v2.visibility = View.GONE
            binding.cardMoreCredit.visibility = View.GONE
            binding.logoutLayout.visibility = View.GONE
            binding.v7.visibility = View.GONE
        } else {
            binding.accountLayout.visibility = View.VISIBLE
            binding.v1.visibility = View.VISIBLE
            binding.orderLayout.visibility = View.VISIBLE
            binding.v2.visibility = View.VISIBLE
            binding.cardMoreCredit.visibility = View.VISIBLE
            binding.logoutLayout.visibility = View.VISIBLE
            binding.v7.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SharedPreferenceHelper.userObj
        if (user != null) {
            val userName = "${user.first_name} ${user.last_name}".trim()
            if (userName.isNotEmpty()) {
                binding.txtUserName.text = userName
            } else {
                // Fallback to email or phone if name is empty
                binding.txtUserName.text = user.email.takeIf { it.isNotEmpty() } ?: user.phone
            }
        }

        val lang = SharedPreferenceHelper.language?.takeIf { it.isNotBlank() }
            ?: Locale.getDefault().language

        val chevronIcon = if (lang == "ar") R.drawable.ic_chevron_left else R.drawable.ic_chevron_right

        binding.imageView6.setImageResource(chevronIcon)
        binding.icProfile.setImageResource(R.drawable.ic_profile)

        binding.imageView12.setImageResource(chevronIcon)
        binding.icClock.setImageResource(R.drawable.ic_clock)

        binding.icArrowCredit.setImageResource(chevronIcon)
        binding.imageView11.setImageResource(chevronIcon)
        binding.icStar.setImageResource(R.drawable.ic_star)

        binding.imageView10.setImageResource(chevronIcon)
        binding.icWorld.setImageResource(R.drawable.ic_world)

        binding.imageView9.setImageResource(chevronIcon)
        binding.icPrivacy.setImageResource(R.drawable.ic_privacy)

        binding.imageView8.setImageResource(chevronIcon)
        binding.icAbout.setImageResource(R.drawable.ic_about)

        binding.imageView7.setImageResource(chevronIcon)
        binding.icContact.setImageResource(R.drawable.ic_phone)

        binding.imageView13.setImageResource(chevronIcon)
        binding.icLogout.setImageResource(R.drawable.ic_logout)


    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.accountLayout.id -> {
                startActivity(Intent(requireContext(), AccountDetailsActivity::class.java))
            }
            binding.orderLayout.id -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, TrackingFragment())
                    .addToBackStack(null)
                    .commit()
            }
            binding.cardMoreCredit.id -> {
                val intent = Intent(requireContext(), CreditInfoActivity::class.java).apply {
                    putExtra("EXTRA_CREDIT_LIMIT", "$creditLimit EGP")
                    putExtra("EXTRA_USED_CREDIT", "$usedCredit EGP")
                    putExtra("EXTRA_AVAILABLE_CREDIT", "$availableCredit EGP")
                }
                startActivity(intent)
            }
            binding.languagesLayout.id -> {
                startActivity(Intent(requireContext(), LanguageActivity::class.java))
            }
            binding.privacyLayout.id -> {
                startActivity(Intent(requireContext(), PrivacyActivity::class.java))
            }
            binding.aboutLayout.id -> {
                startActivity(Intent(requireContext(), AboutUsActivity::class.java))
            }
            binding.contactLayout.id -> {
                startActivity(Intent(requireContext(), ContactUsActivity::class.java))
            }
            binding.logoutLayout.id -> {
                SharedPreferenceHelper.apply {
                    isLogged = false
                    userObj = null
                    userToken = null
                    language = "en"
                }

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateCreditData(limit: Double, used: Double, available: Double) {
        creditLimit = limit
        usedCredit = used
        availableCredit = available
        Log.d("CreditInfoData", "Data Updated -> Limit: $limit, Used: $used, Available: $available")
    }
}