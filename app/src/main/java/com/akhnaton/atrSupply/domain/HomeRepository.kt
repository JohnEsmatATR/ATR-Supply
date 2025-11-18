package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.IProducts
import com.akhnaton.atrSupply.shared.RetrofitClient

class HomeRepository {
    private val retrofit = RetrofitClient.getInstance(IProducts::class.java)

    suspend fun getCategory( categories: String) = retrofit.getCategories(categories)

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

    suspend fun searchProduct(word: String?="",categoryId: String, categories: Int) = retrofit.searchProduct(
        word,categoryId ,categories
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

    suspend fun addProductToFavorites(token: String, productId: Int, add: Boolean, categories: String)
    = retrofit.addFavoriteProduct(token, productId,add,categories)

    suspend fun deleteProductToFavorites(token: String, productId: Int, add: Boolean,categories: String)
            = retrofit.deleteFromFav(token, productId,add,categories)

}