package com.akhnaton.atrapp.domain

import android.util.Log
import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.data.model.ListCategoryModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import retrofit2.Response

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory() = retrofit.getCategories("token")

    suspend fun getBestSeller(
    ) = retrofit.getBestSeller(
        "Bearer",
        1,
    )

    suspend fun getProduct(
        categoryId: Int,
    ) = retrofit.getProducts(
        "Bearer ",
        categoryId,
    )

    suspend fun getFavorite() = retrofit.getFavorite(
        "Bearer ",
        1,
    )

    suspend fun getProductDetails(
        productId: Int,
    ) = retrofit.getProductDetails(
        "Bearer ",
        productId,
    )

}