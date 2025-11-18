package com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode

sealed class RegisterFromLineIntent {
    data class Register ( val code : String,
                 val phone : String,
                 val firstName : String,
                 val lastName : String,
                 val email : String,
                 val fbToken : String,
                 val password : String) : RegisterFromLineIntent()
}