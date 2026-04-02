package com.akhnaton.atrapp.ui.nav.home.notifications

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.NotificationModel
import com.akhnaton.atrapp.databinding.ActivityNotificationsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class NotificationsActivity : BaseActivity() {
    lateinit var binding: ActivityNotificationsBinding
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

        val list = ArrayList<NotificationModel>()
        list.add(NotificationModel("COD Request Confirmed", "The COD payment method for you order 913793764AHJD has been approved", "22/02/2024" ,R.drawable.test_profile))
        list.add(NotificationModel("COD Request Confirmed", "The COD payment method for you order 913793764AHJD has been approved", "22/02/2024" ,R.drawable.test_profile))
        list.add(NotificationModel("COD Request Confirmed", "The COD payment method for you order 913793764AHJD has been approved", "22/02/2024" ,R.drawable.test_profile))
        list.add(NotificationModel("COD Request Confirmed", "The COD payment method for you order 913793764AHJD has been approved", "22/02/2024" ,R.drawable.test_profile))
        list.add(NotificationModel("COD Request Confirmed", "The COD payment method for you order 913793764AHJD has been approved", "22/02/2024" ,R.drawable.test_profile))

        setupReviewRecycler(list)

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun setupReviewRecycler(list: List<NotificationModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        notificationAdapter = NotificationAdapter(
            onClick = { item, position -> },
        )
        notificationAdapter.setData(list)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = notificationAdapter
    }

}