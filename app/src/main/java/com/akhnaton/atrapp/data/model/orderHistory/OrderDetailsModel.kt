package com.akhnaton.atrapp.data.model.orderHistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetailsModel(
    val id: Int,
    @SerializedName("TITLE")
    val name: String,
    @SerializedName("DESCRIPTION")
    val description: String,
    @SerializedName("QUANTITY")
    val quantity: String?,
    @SerializedName("ITEM_TOTAL_PRICE")
    val price: Double,
    val discountPrice: Double,
    @SerializedName("IMAGE_URL")
    val img: String,
) : Parcelable