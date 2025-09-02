package com.akhnaton.atrapp.ui.nav.home.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsStatus
import com.akhnaton.atrapp.domain.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    val homeIntent = Channel<ProductsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProductsStatus>(ProductsStatus.Idle)
    val state: StateFlow<ProductsStatus> get() = _state

    // pagination values
    var currentPage = 1
    private val limit = 1000
    private var isLoading = false
    private var isLastPage = false

    init {
        makeProductsObserve()
    }
    private fun makeProductsObserve() {
        viewModelScope.launch {
            homeIntent.consumeAsFlow().collect {
                when (it) {
                    is ProductsIntent.GetProducts -> getProductsBasedOnCategoryRepo(it.categoryId, it.categoriesName)
                }
            }
        }
    }



    private fun getProductsBasedOnCategoryRepo(categoryId: Int, categoryName : String) {
        viewModelScope.launch {
            isLoading = true
            _state.value = ProductsStatus.Loading
            _state.value = try {
                val response = HomeRepository().getProductsByPagination(categoryId, currentPage, limit,categoryName)
                if (response.code() == 200) {
                    val data = response.body()!!
                    if (data.data!!.size < limit) {
                        isLastPage = true
                    }
                    if (!isLastPage) currentPage++
                    ProductsStatus.GetProducts(data)
                } else {
                    ProductsStatus.Error(response.message())
                }
            } catch (e: Exception) {
                ProductsStatus.Error(e.message)
            }
            isLoading = false
        }
    }

}


