package com.akhnaton.atrapp.domain

import android.content.pm.PackageInfo
import android.util.Log
import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.data.model.ListCategoryModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import retrofit2.Response

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory(version: String) = retrofit.getCategories(
        version,
    )

    suspend fun getBestSeller(
        version: String,
    ) = retrofit.getBestSeller(
        version,
        1,
    )

    suspend fun getProduct(
        version: String,
        categoryId: Int,
    ) = retrofit.getProducts(
        version,
        categoryId,
    )

    suspend fun getFavorite(version: String,) = retrofit.getFavorite(
        version,
        1,
    )

    suspend fun getProductDetails(
        version: String,
        productId: Int,
    ) = retrofit.getProductDetails(
        version,
        productId,
    )

}