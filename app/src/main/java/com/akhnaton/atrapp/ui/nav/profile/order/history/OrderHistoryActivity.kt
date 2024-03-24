package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.databinding.ActivityOrderHistoryBinding
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderHistoryActivity : AppCompatActivity(), OrderHistoryAdapter.OnProductClickListener,
    View.OnClickListener {
    private lateinit var binding: ActivityOrderHistoryBinding
    private var mAdapter = OrderHistoryAdapter()
    private var mList = mutableListOf<OrderHistoryModel>()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        fillList()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history)

        binding.orderRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@OrderHistoryActivity, LinearLayoutManager.VERTICAL, false)
        }

        binding.orderRecycler.adapter = mAdapter
        binding.fromLayout.setOnClickListener(this)
        binding.fromED.setOnClickListener(this)
        binding.toLayout.setOnClickListener(this)
        binding.toED.setOnClickListener(this)
    }


    private fun fillList() {
        val l1 = OrderHistoryModel(1, 1000.0, "2", "1", "Giza, Egypt", "Pending", 0)
        val l2 = OrderHistoryModel(1, 1000.0, "2", "1", "Giza, Egypt", "Delivered", 1)
        val l3 = OrderHistoryModel(1, 1000.0, "2", "1", "Giza, Egypt", "Pending", 0)
        val l4 = OrderHistoryModel(1, 1000.0, "8", "6", "Giza, Egypt", "Delivered", 1)

        mList.add(l1)
        mList.add(l2)
        mList.add(l3)
        mList.add(l4)
        mAdapter.setData(mList, this)
    }

    override fun onProductClick(data: OrderHistoryModel) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        showDatePicker(v.id)
    }


    private fun showDatePicker(id: Int) {
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePickerDialog,
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                if (binding.fromED.id == id) {
                    binding.fromED.setText(formattedDate.toString())
                } else {
                    binding.toED.setText(formattedDate.toString())
                }


            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}