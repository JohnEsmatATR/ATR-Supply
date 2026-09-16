package com.akhnaton.atrapp.data.model.orderHistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OrderHistoryModel(
    @SerializedName("ORDER_ID")
    val ORDER_ID: String? = null,

    @SerializedName("STATUS_GROUP")
    val statusGroup: List<StatusGroup>? = null
) : Parcelable


@Parcelize
data class StatusGroup(
    @SerializedName("NAME")
    val name: String? = null,

    @SerializedName("DATE")
    val date: String? = null,

    @SerializedName("DESCRIPTION")
    val DESCRIPTION: String? = null,

    @SerializedName("IS_COMPLETED")
    val isCompleted: Boolean? = false,

    @SerializedName("COLOR")
    val color: String? = null
) : Parcelable