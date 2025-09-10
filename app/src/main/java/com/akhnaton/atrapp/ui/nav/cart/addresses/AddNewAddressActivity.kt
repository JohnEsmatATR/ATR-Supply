package com.akhnaton.atrapp.ui.nav.cart.addresses

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.MapModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.databinding.ActivityAddNewAddressBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsActivity
import com.akhnaton.atrapp.ui.nav.profile.order.history.MyOrdersViewModel
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderHistoryAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewAddressActivity : BaseActivity()  {
    lateinit var binding: ActivityAddNewAddressBinding
//    private val addressesViewModel: AddressesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun onClick() {
        binding.btnAdd.setOnClickListener {
//            addNewAddress()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}