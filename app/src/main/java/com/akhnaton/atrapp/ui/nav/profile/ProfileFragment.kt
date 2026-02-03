package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.FragmentProfileBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.onBoarding.OnBoardingActivity
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderHistoryActivity
import com.akhnaton.atrapp.ui.nav.tracking.TrackingFragment
import java.util.Locale


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
        binding.logoutLayout.setOnClickListener(this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Display username
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

        if (lang == "ar") {
            binding.imageView6.setImageResource(R.drawable.ic_profile)
            binding.icProfile.setImageResource(R.drawable.ic_chevron_left)

            // order layout
            binding.imageView12.setImageResource(R.drawable.ic_clock)
            binding.icClock.setImageResource(R.drawable.ic_chevron_left)

            // rate layout
            binding.imageView11.setImageResource(R.drawable.ic_star)
            binding.icStar.setImageResource(R.drawable.ic_chevron_left)

            binding.imageView10.setImageResource(R.drawable.ic_world)
            binding.icWorld.setImageResource(R.drawable.ic_chevron_left)

            binding.imageView9.setImageResource(R.drawable.ic_privacy)
            binding.icPrivacy.setImageResource(R.drawable.ic_chevron_left)

            binding.imageView8.setImageResource(R.drawable.ic_about)
            binding.icAbout.setImageResource(R.drawable.ic_chevron_left)

            binding.imageView7.setImageResource(R.drawable.ic_phone)
            binding.icContact.setImageResource(R.drawable.ic_chevron_left)

            binding.imageView13.setImageResource(R.drawable.ic_logout)
            binding.icLogout.setImageResource(R.drawable.ic_chevron_left)
        } else {
            binding.imageView6.setImageResource(R.drawable.ic_chevron_right)
            binding.icProfile.setImageResource(R.drawable.ic_profile)


            // order layout
            binding.imageView12.setImageResource(R.drawable.ic_chevron_right)
            binding.icClock.setImageResource(R.drawable.ic_clock)

            // rate layout
            binding.imageView11.setImageResource(R.drawable.ic_chevron_right)
            binding.icStar.setImageResource(R.drawable.ic_star)

            binding.imageView10.setImageResource(R.drawable.ic_chevron_right)
            binding.icWorld.setImageResource(R.drawable.ic_world)

            binding.imageView9.setImageResource(R.drawable.ic_chevron_right)
            binding.icPrivacy.setImageResource(R.drawable.ic_privacy)

            binding.imageView8.setImageResource(R.drawable.ic_chevron_right)
            binding.icAbout.setImageResource(R.drawable.ic_about)

            binding.imageView7.setImageResource(R.drawable.ic_chevron_right)
            binding.icContact.setImageResource(R.drawable.ic_phone)

            binding.imageView13.setImageResource(R.drawable.ic_chevron_right)
            binding.icLogout.setImageResource(R.drawable.ic_logout)

        }
    }

    override fun onClick(v: View) {
        if (v.id == binding.accountLayout.id) {
            val intent = Intent(requireContext(), AccountDetailsActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.orderLayout.id) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, TrackingFragment())
                .addToBackStack(null)
                .commit()
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

            val intent = Intent(requireContext(), OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

    }


}