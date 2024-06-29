package com.akhnaton.atrapp.data.statuesValue.auth.login

sealed class LoginIntent {

    data class Login(
        val email: String,
        val password: String,
    ) : LoginIntent()
}