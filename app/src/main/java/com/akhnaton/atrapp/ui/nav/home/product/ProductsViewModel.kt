package com.akhnaton.atrapp.ui.nav.home.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    val homeIntent = Channel<ProductsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProductsStatus>(ProductsStatus.Idle)

    val state: StateFlow<ProductsStatus> get() = _state

    init {
        makeProductsObserve()
    }

    private fun makeProductsObserve() {
        viewModelScope.launch {
            homeIntent.consumeAsFlow().collect {
                when (it) {
                    is ProductsIntent.GetProducts -> getProductsBasedOnCategoryRepo(
                        it.version,
                        it.categoryId,
                    )
                }
            }
        }
    }

    private fun getProductsBasedOnCategoryRepo(
        version: String,
        categoryId: Int,
    ) {
        viewModelScope.launch {
            _state.value = ProductsStatus.Loading
            _state.value = try {
                val response = HomeRepository().getProduct(version, categoryId)
                if (response.code() == 200) {
                    ProductsStatus.GetProducts(response.body()!!)
                } else {
                    ProductsStatus.Error(response.message())
                }
            } catch (e: Exception) {
                ProductsStatus.Error(e.message)
            }
        }
    }

}

