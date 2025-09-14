package com.akhnaton.atrapp.data.model

import com.google.gson.annotations.SerializedName

data class CartApiResponse(
    val status: Int,
    val message: String,
    val data: List<CartData>
)

data class CartData(
    val carts: List<CartResponse>,
    @SerializedName("total_carts_data")
    val totalCartsData: TotalCartsData
)

data class CartResponse(
    @SerializedName("order_type")
    val orderType: String,
    @SerializedName("order_type_txt")
    val orderTypeTxt: String,
    @SerializedName("order_type_lang")
    val orderTypeLang: OrderTypeLang,
    val items: CartItems
)

data class OrderTypeLang(
    val en: String,
    val ar: String
)

data class CartItems(
    val pagination: Pagination,
    @SerializedName("data")
    val products: List<CartProduct>,
    val params: List<Any>,
    @SerializedName("extra_data")
    val extraData: ExtraData?
)

data class Pagination(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("page_size")
    val pageSize: Int
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
    @SerializedName("ITEM_TYPE")
    val itemType: String,
    @SerializedName("QUANTITY")
    val quantity: Int,
    @SerializedName("MY_QUANTITY")
    val myQuantity: Int,
    @SerializedName("WEIGHT")
    val weight: String,
    @SerializedName("QOUTA")
    val quota: Int,
    @SerializedName("TAX")
    val tax: Double,
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
    val bonusQuantity: String
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

data class TotalCartsData(
    @SerializedName("cart_total")
    val cartTotal: Double,
    @SerializedName("total_discount")
    val totalDiscount: Double
)
