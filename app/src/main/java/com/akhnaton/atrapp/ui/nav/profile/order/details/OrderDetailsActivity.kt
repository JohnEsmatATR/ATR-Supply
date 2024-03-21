package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.databinding.ActivityOrderDetailsBinding
import com.akhnaton.atrapp.ui.nav.profile.order.details.returnOrder.OrderReturnActivity

class OrderDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOrderDetailsBinding
    private var mAdapter = OrderDetailsAdapter()
    private var mList = mutableListOf<OrderDetailsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        fillList()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)
        binding.returnItemsLayout.setOnClickListener(this)
        binding.productRecycler.apply {
            layoutManager =
                LinearLayoutManager(
                    this@OrderDetailsActivity,
                    LinearLayoutManager.VERTICAL, false
                )
        }
        binding.productRecycler.adapter = mAdapter
    }

    private fun fillList() {
        val p1 =
            OrderDetailsModel(0, "Eva Hand soap royal Deep Lines Filler 50 ML", 10, 70.0, 200.0)
        val p2 =
            OrderDetailsModel(1, "Eva Hand soap royal Deep Lines Filler 30 ML", 10, 120.0, 300.0)
        val p3 =
            OrderDetailsModel(2, "Eva Hand soap royal Deep Lines Filler 40 ML", 10, 20.0, 60.0)
        val p4 =
            OrderDetailsModel(3, "Eva Hand soap royal Deep Lines Filler 60 ML", 10, 10.0, 80.0)
        mList.add(p1)
        mList.add(p2)
        mList.add(p3)
        mList.add(p4)
        mAdapter.setData(mList)
    }

    override fun onClick(v: View) {
        if (v.id == binding.returnItemsLayout.id) {
            val intent = Intent(this@OrderDetailsActivity,OrderReturnActivity::class.java)
            startActivity(intent)
        }
    }
}