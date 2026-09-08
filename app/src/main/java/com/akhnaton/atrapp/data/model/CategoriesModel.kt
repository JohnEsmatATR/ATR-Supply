package com.akhnaton.atrapp.data.model

import java.io.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CategoriesModel(
    val CATEGORY_ID: Int? = null,
    val CATEGORY_NAME: String? = null,
    val ID: Int? = null,
    val TITLE: String? = null,
    val IMAGE_URL: String? = null,
    val CHILD_ID: String? = null,
    var selected: Boolean = false
) : Serializable


