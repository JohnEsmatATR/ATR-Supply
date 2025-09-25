package com.akhnaton.atrapp.shared

object ConstantLinks {
    //in site company use http

    //const val BASE_URL = "http://sales.atr-eg.com/customer_test/"   //test
    // out site company use https

   const val BASE_URL = "http://10.42.151.27/customer_test/"          // test
   // const val BASE_URL = "https://sales.atr-eg.com/customer/"     // Prod

    // Auth
    const val LOGIN = "user/login"
    const val REGISTER = "user/register?debug=1"
    const val SEND_OTP = "user/send_otp"
    // forget password
    const val CHECK_OTP = "user/check_otp"
    const val CHANGE_PASSWORD = "user/change_password"
    const val LOGOUT = "auth/logout"

    // categories & products
    const val GET_CATEGORY_LIST = "category/list"
    const val GET_ALL_PRODUCT = "product/list"

    // cart
    const val ADD_TO_CART = "cart/add"
    const val GET_MY_CART = "cart/my_cart"
    const val CHECKOUT = "cart/check_out"
    const val MY_ORDERS = "cart/my_orders"
    const val MY_ORDERS_DETAILS = "cart/my_order_details"
    const val DELETE_FROM_CART = "cart/add"
    const val PAYMENT_TYPE = "user/get_customer_payment_type"

    // review
    const val GET_REVIEWS = "product/product_reviews"
    const val ADD_REVIEW = "product/add_review"

    // fav
    const val GET_FAV_PRODUCT = "product/add_fav?debug=1"
    const val DELETE_FROM_FAV = "product/delete_fav"

    // get user address
    const val CUSTOMER_ADDRESS = "user/get_customer_sites"
    const val CHANGE_DEFAULT_ADDRESS= "user/change_default_site"

    //home panner
    const val PANNER ="home/banner"

    //order types and categories
    const val ORDER_TYPES= "category/order_type_and_category_list"


    //REGISTER WITH CUSTOMER CODE
    const val SENT_OTP = "User/send_sms_otp"
    const val VALIDATE_OTP = "User/validate_OTP"
    const val REGISTER_FROM_LINE = "user/register_user_from_lines"
    const val APP_SETTINGS = "home/app_settings"
}
