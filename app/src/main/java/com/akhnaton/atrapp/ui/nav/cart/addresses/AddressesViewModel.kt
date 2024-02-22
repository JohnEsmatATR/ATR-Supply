package com.akhnaton.atrapp.ui.nav.cart.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AddressesViewModel : ViewModel()  {
//    val addressIntent = Channel<AddressIntent>(Channel.UNLIMITED)
//
//    private val _state = MutableStateFlow<AddressStatus>(AddressStatus.Idle)
//
//    val state: StateFlow<AddressStatus> get() = _state
//
//    init {
//        makeHomeObserve()
//    }
//
//    private fun makeHomeObserve() {
//        viewModelScope.launch {
//            addressIntent.consumeAsFlow().collect {
//                when (it) {
//                    is AddressIntent.GetMyAddresses -> getAddresses(
//                        it.token!!,
//                    )
//
//                    is AddressIntent.AddUserAddress -> addUserAddress(
//                        it.token,
//                        it.address,
//                        it.landmark,
//                        it.receiverName,
//                        it.receiverPhone,
//                        it.floorNumber,
//                        it.apartmentNumber,
//                        it.cityId,
//                        it.countryId,
//                        it.areaId,
//                        it.prime,
//                    )
//
//                    is AddressIntent.MakeAddressPrime -> makeAddressPrime(
//                        it.token,
//                        it.addressId
//                    )
//
//                }
//            }
//        }
//    }
//
//
//
//    private fun getAddresses(
//        token: String,
//    ) {
//        viewModelScope.launch {
//            _state.value = AddressStatus.Loading
//            _state.value = try {
//                val response = AddressRepository().getMyAddresses(token)
//                if (response.code() == 200) {
//                    AddressStatus.GetMyAddresses(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<AddressesModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<AddressesModel, List<String>>>() {}.type
//                    )
//                    AddressStatus.GetMyAddresses(theList)
//                }
//
//            } catch (e: Exception) {
//                AddressStatus.Error(e.message)
//            }
//
//        }
//    }
//
//    private fun addUserAddress (
//        token: String?,
//        address: String?,
//        landmark: String?,
//        receiverName: String?,
//        receiverPhone: String?,
//        floorNumber: Int?,
//        apartmentNumber: Int?,
//        cityId: Int?,
//        countryId: Int?,
//        areaId: Int?,
//        prime: Int?,
//    ) {
//        viewModelScope.launch {
//            _state.value = AddressStatus.Loading
//            _state.value = try {
//                val response = AddressRepository().addUserAddress(
//                    token,
//                    address,
//                    landmark,
//                    receiverName,
//                    receiverPhone,
//                    floorNumber,
//                    apartmentNumber,
//                    cityId,
//                    countryId,
//                    areaId,
//                    prime,
//                )
//                if (response.code() == 200) {
//                    AddressStatus.AddUserAddress(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<AddNewAddressModel, ErrorAddressesModel>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<AddNewAddressModel, ErrorAddressesModel>>() {}.type
//                    )
//                    AddressStatus.AddUserAddress(theList)
//                }
//
//            } catch (e: Exception) {
//                AddressStatus.Error(e.message)
//            }
//
//        }
//    }
//
//
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
//

}