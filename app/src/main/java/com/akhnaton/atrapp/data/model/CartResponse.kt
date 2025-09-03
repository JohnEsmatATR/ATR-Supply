package com.akhnaton.atrapp.data.model

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("order_type")
    val orderType: String,
    val order_type_txt : String,
    val items: CartItems
)

data class CartItems(
    @SerializedName("data")
    val products: List<CartProduct>,
    @SerializedName("extra_data")
    val extraData: ExtraData?
)

data class CartProduct(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("TITLE")
    val title: String,
    @SerializedName("DESCRIPTION")
    val description: String,
    @SerializedName("IMAGE_URL")
    val imageUrl: String,
    @SerializedName("QUANTITY")
    val quantity: Int,
    @SerializedName("MY_QUANTITY")
    val myQuantity: Int,
    @SerializedName("WEIGHT")
    val weight: String,
    @SerializedName("QOUTA")
    val quota: Int,
    @SerializedName("TAX")
    val tax: Int,
    @SerializedName("IS_BEST_SELLER")
    val isBestSeller: Boolean,
    @SerializedName("IS_LIKED")
    val isLiked: Boolean,
    @SerializedName("PRICE_WITHOUT_TAX")
    val priceWithoutTax: Double,
    @SerializedName("PRICE_DISCOUNT")
    val priceDiscount: Double,
    @SerializedName("PRICE_DISCOUNT_PERCENTAGE")
    val priceDiscountPercentage: String,
    @SerializedName("PRICE_AFTER_DISCOUNT")
    val priceAfterDiscount: Double,
    @SerializedName("PRICE_WITH_TAX")
    val priceWithTax: Double,
    @SerializedName("MY_QUANTITY_TOTAL_PRICE")
    val myQuantityTotalPrice: Double,
    @SerializedName("BONUS_DATA")
    val bonusData: List<Any>,
    @SerializedName("REVIEWS")
    val reviews: Reviews,
    @SerializedName("IN_STOCK")
    val inStock: Boolean,
    @SerializedName("BONUS_QUANTITY")
    val bodus_quantity : Int
)

data class Reviews(
    @SerializedName("RATE")
    val rate: Double?,
    @SerializedName("REVIEWS_COUNT")
    val reviewsCount: Int?
)

data class ExtraData(
    @SerializedName("cart_total")
    val cartTotal: Double,
    @SerializedName("shipping_fees")
    val shippingFees: Double,
    @SerializedName("total_discount")
    val totalDiscount: Double,
    @SerializedName("not_found")
    val notFound: List<Any>
)
