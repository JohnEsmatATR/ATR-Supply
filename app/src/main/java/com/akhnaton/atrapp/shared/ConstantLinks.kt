package com.akhnaton.atrapp.shared

object ConstantLinks {
    const val BASE_URL = "https://sales.atr-eg.com/customer_test/"          // test
//    const val BASE_URL = "https://sales.atr-eg.com/customer/"     // Prod

    // Auth
    const val LOGIN = "user/login"
    const val REGISTER = "user/register"
    const val SEND_OTP = "user/send_otp"
    // forget password
    const val CHECK_OTP = "user/check_otp"
    const val CHANGE_PASSWORD = "user/change_password"
    const val LOGOUT = "auth/logout"

    // categories & products
    const val GET_CATEGORIES = "category/list"
    const val GET_ALL_PRODUCT = "product/list"

    // cart
    const val ADD_TO_CART = "cart/add"
    const val GET_MY_CART = "cart/my_cart"

    // review
    const val GET_REVIEWS = "product/product_reviews"
    const val ADD_REVIEW = "product/add_review"
}