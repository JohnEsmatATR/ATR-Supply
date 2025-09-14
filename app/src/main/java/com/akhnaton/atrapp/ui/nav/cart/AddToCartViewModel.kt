package com.akhnaton.atrapp.ui.nav.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AddToCartViewModel : ViewModel() {

    val addToCartIntent = Channel<AddToCartIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AddToCartStatus>(AddToCartStatus.Idle)

    val state: StateFlow<AddToCartStatus> get() = _state



    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            addToCartIntent.consumeAsFlow().collect {
                when (it) {
                    is AddToCartIntent.AddProductToCart -> addProductToCart(
                        it.productId,
                        it.quantity,
                        it.category
                    )

                    is AddToCartIntent.deleteProductToCart -> deleteProductFromCart(
                        it.productId,
                        it.quantity
                    )


                }
            }
        }
    }


    private fun addProductToCart(
        productId: Int?,
        quantity: Int?,
        category: String
    ) {
        viewModelScope.launch {
            _state.value = AddToCartStatus.Loading
            _state.value = try {
                val response = CartRepository().addProductToCart(
                    productId,
                    quantity,
                    category
                )
                if (response.code() == 200) {
                    AddToCartStatus.AddToCart(response.body()!!)
                } else {
                    AddToCartStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                AddToCartStatus.Error(e.message)
            }

        }
    }

    private fun deleteProductFromCart(
        productId: Int?,
        quantity: Int?,
    ) {
        viewModelScope.launch {
            _state.value = AddToCartStatus.Loading
            _state.value = try {
                val response = CartRepository().deleteProductFromCart(
                    productId,
                    quantity,
                )
                if (response.code() == 200) {
                    AddToCartStatus.AddToCart(response.body()!!)
                } else {
                    AddToCartStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                AddToCartStatus.Error(e.message)
            }

        }
    }


}

