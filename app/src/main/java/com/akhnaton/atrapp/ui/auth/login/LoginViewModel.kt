package com.akhnaton.atrapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.auth.LoginModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginIntent
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginStatus
import com.akhnaton.atrapp.domain.AuthRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginStatus>(LoginStatus.Idle)

    val state: StateFlow<LoginStatus> get() = _state

    init {
        makeLogin()
    }

    private fun makeLogin() {
        viewModelScope.launch {
            loginIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.Login -> loginRepo(
                        it.email,
                        it.password,
                        it.fbToken
                    )
                }
            }
        }
    }

    private fun loginRepo(
        email: String,
        password: String,
        fbToken : String
    ) {
        viewModelScope.launch {
            _state.value = LoginStatus.Loading
            _state.value = try {
                val response = AuthRepository().login(email, password,fbToken)
                if (response.code() == 200) {
                    LoginStatus.Login(response.body()!!)
                } else {
                    LoginStatus.Error(response.body()!!.message)
                }
            } catch (e: Exception) {
                LoginStatus.Error(e.message)
            }
        }
    }
}