package com.akhnaton.atrapp.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderTypeModel(
    val IMAGE_URL: String,
    val order_type: String,
    val order_type_index: String,
    val categories: List<CategoriesModel>,
    val selectedCount: Int
) : Parcelable

