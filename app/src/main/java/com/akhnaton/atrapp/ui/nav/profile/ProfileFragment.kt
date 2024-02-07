package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.akhnaton.atrapp.databinding.FragmentProfileBinding
import com.akhnaton.atrapp.shared.BaseFragment
import org.w3c.dom.Comment


class ProfileFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        init()
        return binding.root
    }

    private fun init() {
//        binding.txtName.text = SharedPreferenceHelper.userObj?.full_name
//        binding.txtUserType.text = SharedPreferenceHelper.userObj?.user_type
//        if (SharedPreferenceHelper.membership.toString() != "0") {
//            binding.txtMembershipId.text = SharedPreferenceHelper.membership.toString()
//        } else {
//            binding.txtIdHashtag.visibility = View.GONE
//            binding.imShare.visibility = View.GONE
//        }
//
//        if (SharedPreferenceHelper.showWallet!!) {
//            binding.layoutWallet.visibility = View.VISIBLE
//        } else {
//            binding.layoutWallet.visibility = View.GONE
//        }
//
//        binding.imShare.setOnClickListener {
//
//            val text = "${Common.ImgUrl}joinus?membership=${binding.txtMembershipId.text}"
//            val intent = Intent(Intent.ACTION_SEND)
//              intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_TEXT, text)
//
//            startActivity(Intent.createChooser(intent, "Share via"))
//        }
//
//        binding.layoutEditProfileInformation.setOnClickListener {
//            startActivity(Intent(requireContext(), EditProfileInformationActivity::class.java))
//        }
//        binding.layoutLanguage.setOnClickListener {
//            startActivity(Intent(requireContext(), LanguageActivity::class.java))
//        }
//        binding.layoutWallet.setOnClickListener {
//            startActivity(Intent(requireContext(), WalletActivity::class.java))
//        }
//        binding.layoutDashboard.setOnClickListener {
//            startActivity(Intent(requireContext(), DashboardActivity::class.java))
//        }
//        binding.layoutReports.setOnClickListener {
//            startActivity(Intent(requireContext(), ReportsActivity::class.java))
//        }
//        binding.layoutOdrers.setOnClickListener {
//            startActivity(Intent(requireContext(), OrdersActivity::class.java))
//        }
//        binding.layoutYourCommission.setOnClickListener {
//            startActivity(Intent(requireContext(), YourCommissionActivity::class.java))
//        }
//        binding.layoutYourCashback.setOnClickListener {
//            startActivity(Intent(requireContext(), YourCashbackActivity::class.java))
//        }
    }

}