package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.akhnaton.atrapp.shared.Common
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {

    val homeIntent = Channel<ProductDetailsIntent>(Channel.UNLIMITED)


    private val _state = MutableStateFlow<ProductDetailsStatus>(ProductDetailsStatus.Idle)

    val state: StateFlow<ProductDetailsStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            homeIntent.consumeAsFlow().collect {
                when (it) {
                    is ProductDetailsIntent.GetProductDetails -> getBestsellerRepo(it.bestSeller, it.categories)

                }
            }
        }
    }

    private fun getBestsellerRepo(productDetails: Int,  categories: String) {
        viewModelScope.launch {
            _state.value = ProductDetailsStatus.Loading
            _state.value = try {
                val response = HomeRepository().getProductDetails(productDetails,categories)
                if (response.code() == 200) {
                    Log.d(Common.KeroDebug, "getBestsellerRepo ${response.body()!!}")
                    ProductDetailsStatus.GetProductDetails(response.body()!!)

                } else {
                    ProductDetailsStatus.Error(response.body()!!.message)
                }
            } catch (e: Exception) {
                ProductDetailsStatus.Error(e.message)
            }
        }
    }

}

