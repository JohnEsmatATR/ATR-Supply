package com.akhnaton.atrapp.ui.nav.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CartViewModel : ViewModel() {

//    val cartIntent = Channel<CartIntent>(Channel.UNLIMITED)
//
//    private val _state = MutableStateFlow<CartStatus>(CartStatus.Idle)
//
//    val state: StateFlow<CartStatus> get() = _state
//
//    init {
//        makeHomeObserve()
//    }
//
//    private fun makeHomeObserve() {
//        viewModelScope.launch {
//            cartIntent.consumeAsFlow().collect {
//                when (it) {
//                    is CartIntent.GetMyCart -> getMyCart(
//                        it.token!!,
//                    )
//
//                    is CartIntent.DeleteProductFromCart -> deleteProductFromCart(
//                        it.token,
//                        it.productId,
//                    )
//
//                    is CartIntent.GetOrderCheckout -> getOrderCheckout(
//                        it.token,
//                        it.sendCheckoutModel,
//                    )
//
//                    is CartIntent.SaveOrderApp -> saveOrderApp(
//                        it.token,
//                        it.saveOrderModel,
//                    )
//
//                    is CartIntent.PaymentWhenPayByCode -> paymentWhenPayByCode(
//                        it.token,
//                        it.orderId,
//                        it.refCode
//                    )
//                    is CartIntent.PaymentWhenPayByVisa -> paymentWhenPayByVisa(
//                        it.token,
//                        it.sendPaymentVisaModel
//                    )
//                }
//            }
//        }
//    }
//
//    private fun getMyCart(
//        token: String,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().getMyCart(token)
//                if (response.code() == 200) {
//                    CartStatus.GetMyCart(response.body()!!)
//                } else if (response.code() == 401) {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<CartModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<CartModel, List<String>>>() {}.type
//                    )
//                    CartStatus.GetMyCart(theList)
//                } else {
//                    CartStatus.Error(response.message())
//                }
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//        }
//    }
//
//    private fun deleteProductFromCart(
//        token: String?,
//        productId: Int?,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().deleteProductFromCart(
//                    token,
//                    productId,
//                )
//                if (response.code() == 200) {
//                    CartStatus.DeleteProductFromCart(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<CartModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<CartModel, List<String>>>() {}.type
//                    )
//                    CartStatus.DeleteProductFromCart(theList)
//                }
//
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//
//        }
//    }
//
//    private fun getOrderCheckout(
//        token: String?,
//        checkoutModel: SendCheckoutModel?,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().getOrderCheckout(
//                    token,
//                    checkoutModel,
//                )
//                if (response.code() == 200) {
//                    CartStatus.GetOrderCheckout(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<GetCheckoutModel, ErrorCheckoutModel>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<GetCheckoutModel, ErrorCheckoutModel>>() {}.type
//                    )
//                    CartStatus.GetOrderCheckout(theList)
//                }
//
//            } catch (e: HttpException) {
//                if (e.code() == 500) {
//                    CartStatus.Error("Sorry, the connection to our server failed")
//                } else {
//                    CartStatus.Error(e.message)
//                }
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//
//        }
//    }
//
//    private fun saveOrderApp(
//        token: String?,
//        saveOrderModel: SaveOrderModel?,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().saveOrderApp(
//                    token,
//                    saveOrderModel,
//                )
//                if (response.code() == 200) {
//                    CartStatus.SaveOrderApp(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<GetSaveOrderModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<GetSaveOrderModel, List<String>>>() {}.type
//                    )
//                    CartStatus.SaveOrderApp(theList)
//                }
//
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//
//        }
//    }
//    private fun paymentWhenPayByCode(
//        token: String?,
//        orderId: String?,
//        refCode: String?,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().paymentWhenPayByCode(
//                    token,
//                    orderId,
//                    refCode,
//                )
//                if (response.code() == 200) {
//                    CartStatus.PaymentWhenPayByCode(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<Any, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<Any, List<String>>>() {}.type
//                    )
//                    CartStatus.PaymentWhenPayByCode(theList)
//                }
//
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//
//        }
//    }
//    private fun paymentWhenPayByVisa(
//        token: String?,
//        sendPaymentVisaModel: SendPaymentVisaModel?,
//    ) {
//        viewModelScope.launch {
//            _state.value = CartStatus.Loading
//            _state.value = try {
//                val response = CartRepository().paymentWhenPayByVisa(
//                    token,
//                    sendPaymentVisaModel,
//                )
//                if (response.code() == 200) {
//                    CartStatus.PaymentWhenPayByVisa(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<Any, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<Any, List<String>>>() {}.type
//                    )
//                    CartStatus.PaymentWhenPayByVisa(theList)
//                }
//
//            } catch (e: Exception) {
//                CartStatus.Error(e.message)
//            }
//
//        }
//    }


}

