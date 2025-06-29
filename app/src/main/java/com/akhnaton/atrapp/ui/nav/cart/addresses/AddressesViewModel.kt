package com.akhnaton.atrapp.ui.nav.cart.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.domain.AddressRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AddressesViewModel : ViewModel()  {
    val addressIntent = Channel<AddressIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AddressStatus>(AddressStatus.Idle)

    val state: StateFlow<AddressStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            addressIntent.consumeAsFlow().collect {
                when (it) {
                    is AddressIntent.GetMyAddresses -> getAddresses(
                        it.customerId,
                    )



                    is AddressIntent.MakeAddressPrime ->{}

                }
            }
        }
    }



    private fun getAddresses(customerId: Int) {
        viewModelScope.launch {
            _state.value = AddressStatus.Loading

            val resultStatus = try {
                val response = AddressRepository().getMyAddresses(customerId)

                if (response.isSuccessful && response.body() != null) {
                    AddressStatus.GetMyAddresses(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val parsedError: BaseModel<List<AddressModel>> = GsonBuilder().create().fromJson(
                        errorBody,
                        object : TypeToken<BaseModel<List<AddressModel>>>() {}.type
                    )
                    AddressStatus.GetMyAddresses(parsedError)
                }
            } catch (e: Exception) {
                AddressStatus.Error(e.message)
            }

            _state.value = resultStatus
        }
    }









//    private fun makeAddressPrime (
//        token: String?,
//        addressId: Int?,
//    ) {
//        viewModelScope.launch {
//            _state.value = AddressStatus.Loading
//            _state.value = try {
//                val response = AddressRepository().makeAddressPrime(
//                    token,
//                    addressId,
//                )
//                if (response.code() == 200) {
//                    AddressStatus.MakeAddressPrime(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<MakeAddressPrimeModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<MakeAddressPrimeModel, List<String>>>() {}.type
//                    )
//                    AddressStatus.MakeAddressPrime(theList)
//                }
//
//            } catch (e: Exception) {
//                AddressStatus.Error(e.message)
//            }
//
//        }
//    }


}