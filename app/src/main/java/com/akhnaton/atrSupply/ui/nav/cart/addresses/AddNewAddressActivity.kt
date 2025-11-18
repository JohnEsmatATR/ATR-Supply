package com.akhnaton.atrSupply.ui.nav.cart.addresses

import android.os.Bundle
import com.akhnaton.atrSupply.databinding.ActivityAddNewAddressBinding
import com.akhnaton.atrSupply.shared.BaseActivity

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