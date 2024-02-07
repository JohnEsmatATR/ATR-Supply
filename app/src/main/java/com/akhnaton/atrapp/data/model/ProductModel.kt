package com.akhnaton.atrapp.data.model

import java.io.Serializable

data class OneProductModel(
    val products: ProductModel,
)

data class ProductsModel(
    val products: List<ProductModel>,
    val NextOffset: Int,
)

data class ProductModel(
    var id: Int,
    val user_id: Int,
    val product_id: Int,
    val flag: Int,
    val full_name: String,
    val name_en: String,
    val name_ar: String,
    val description_en: String,
    val description_ar: String,
    val image: String,
    val oracle_short_code: String,
    val discount_rate: Double,
    var old_price: Double,
    var price_before_discount: Double,
    var old_discount: Double,
    var price: Double,
    val price_after_discount: Double,
    val quantity: Int,
    var in_favourite: Boolean,
    val excluder_flag: String,
    val stock_status: String,
    val stock_code: Int = -1,
    val is_best_sale: Int = 0,

    val month: String,
    val start_date: String,
    val end_date: String,
    val status: String,
    val total_price: Double,
    val total_old_price: Double,
    val created_at: String,
    val updated_at: String,
    val userRedeemGift: Boolean,
) : Serializable