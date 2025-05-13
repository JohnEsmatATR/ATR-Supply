package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory() = retrofit.getCategories()

    suspend fun getBestSeller(bestSeller: Int) = retrofit.getBestSeller(bestSeller)

    suspend fun getProduct(
        categoryId: Int,
    ) = retrofit.getProducts(
        categoryId,
    )

    suspend fun getFavorite() = retrofit.getFavorite(
        1,
    )

    suspend fun searchProduct(word: String,) = retrofit.searchProduct(
        word,
    )

    suspend fun getProductDetails(
        productId: Int,
    ) = retrofit.getProductDetails(
        productId,
    )

}