package com.akhnaton.atrapp.ui.nav.profile.order.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhnaton.atrapp.domain.OrderStateRepository

class OrderStatesViewModelFactory(
    private val repository: OrderStateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderStatesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderStatesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
