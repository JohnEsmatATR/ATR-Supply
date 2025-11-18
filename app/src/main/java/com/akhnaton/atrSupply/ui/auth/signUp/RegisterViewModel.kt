package com.akhnaton.atrSupply.ui.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.auth.register.RegisterIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.register.RegisterStatus
import com.akhnaton.atrSupply.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewModel : ViewModel() {

    val registerIntent = Channel<RegisterIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<RegisterStatus>(RegisterStatus.Idle)

    val state: StateFlow<RegisterStatus> get() = _state

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            registerIntent.consumeAsFlow().collect {
                when (it) {
                    is RegisterIntent.Register -> addProductToFavourites(
                        it.first_name,
                        it.last_name,
                        it.email,
                        it.phone_number,
                        it.password,
                        it.address_title,
                        it.address,
                        it.latitude,
                        it.longitude,
                        it.firebase_token,
                        it.attach_identity,
                        it.attach_coomercial_register,
                        it.attach_ownership,
                        it.attach_tax,
                        it.attach_license,
                    )
                }
            }
        }
    }


    private fun addProductToFavourites(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        phone_number: RequestBody,
        password: RequestBody,
        address_title: RequestBody,
        address: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        firebase_token: RequestBody,
        attach_identity: MultipartBody.Part,
        attach_coomercial_register: MultipartBody.Part,
        attach_ownership: MultipartBody.Part,
        attach_tax: MultipartBody.Part,
        attach_license: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _state.value = RegisterStatus.Loading
            _state.value = try {
                val response = AuthRepository().register(
                    first_name,
                    last_name,
                    email,
                    phone_number,
                    password,
                    address_title,
                    address,
                    latitude,
                    longitude,
                    firebase_token,
                    attach_identity,
                    attach_coomercial_register,
                    attach_ownership,
                    attach_tax,
                    attach_license,
                )
                if (response.code() == 200) {
                    RegisterStatus.Register(response.body()!!)
                } else {
                    RegisterStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                RegisterStatus.Error(e.message)
            }

        }
    }

}

