package com.akhnaton.atrSupply.data.statuesValue.auth.login

sealed class LoginIntent {

    data class Login(
        val email: String,
        val password: String,
        val fbToken : String
    ) : LoginIntent()
}