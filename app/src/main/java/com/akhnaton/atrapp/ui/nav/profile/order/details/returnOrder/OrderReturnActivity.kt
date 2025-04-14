package com.akhnaton.atrapp.ui.nav.profile.order.details.returnOrder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.databinding.ActivityOrderReturnBinding
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsAdapter

class OrderReturnActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOrderReturnBinding
    private var mAdapter = OrderReturnAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }


    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_return)
        binding.returnRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@OrderReturnActivity, LinearLayoutManager.VERTICAL, false)
        }

        binding.returnRecycler.adapter = mAdapter
        binding.btnBack.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }

    }
}