package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.shared.RetrofitClient

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory() = retrofit.getCategories()

    suspend fun filterProduct(search : String? = "" , categoryId: Int) = retrofit.filterProduct(search, categoryId)

    suspend fun getBestSeller(bestSeller: Int) = retrofit.getBestSeller(bestSeller)

    suspend fun getProduct(
        categoryId: Int,
    ) = retrofit.getProducts(
        categoryId,
    )

    suspend fun getFavorite() = retrofit.getFavorite(
        1,
    )

    suspend fun searchProduct(word: String?="",categoryId: Int?=null) = retrofit.searchProduct(
        word,categoryId
    )
    suspend fun getProductsByPagination(categoryId: Int, page: Int, limit: Int,categories : String) =
        retrofit.getProductsByPagination(categoryId, page, limit,categories)


    suspend fun getProductDetails(
        productId: Int,
        categories: String
    ) = retrofit.getProductDetails(
        productId,
        categories
    )

    suspend fun addProductToFavorites(token: String, productId: Int, add: Boolean)
    = retrofit.addFavoriteProduct(token, productId,add)

    suspend fun deleteProductToFavorites(token: String, productId: Int, add: Boolean)
            = retrofit.deleteFromFav(token, productId,add)

}