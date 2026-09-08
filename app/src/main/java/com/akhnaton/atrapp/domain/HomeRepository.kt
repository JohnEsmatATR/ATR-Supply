package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.shared.RetrofitClient

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory(categories: String) = retrofit.getCategories(categories)

    suspend fun filterProduct(search: String? = "", categoryId: Int) =
        retrofit.filterProduct(search, categoryId)

    suspend fun getBestSeller(bestSeller: Int, orderType: String) = retrofit.getBestSeller(bestSeller, orderType)

    suspend fun getProduct(
        categoryId: Int,
    ) = retrofit.getProducts(
        categoryId,
    )

    suspend fun getFavorite() = retrofit.getFavorite(
        1,
    )

    suspend fun searchProduct(
        word: String? = "",
        categoryId: String,
        categories: Int,
        page: Int,
        limit: Int
    ) = retrofit.searchProduct(
        word, categoryId, categories, page, limit
    )

    suspend fun getProductsByPagination(
        categoryId: Int,
        page: Int,
        limit: Int,
        categories: String,
        sortBy: String? = null // malak
    ) =
        retrofit.getProductsByPagination(categoryId, page, limit, categories, sortBy)


    suspend fun getProductDetails(
        productId: Int,
        categories: String
    ) = retrofit.getProductDetails(
        productId,
        categories
    )

    suspend fun addProductToFavorites(
        token: String,
        productId: Int,
        add: Boolean,
        categories: String
    ) = retrofit.addFavoriteProduct(token, productId, add, categories)

    suspend fun deleteProductToFavorites(
        token: String,
        productId: Int,
        add: Boolean,
        categories: String
    ) = retrofit.deleteFromFav(token, productId, add, categories)

}