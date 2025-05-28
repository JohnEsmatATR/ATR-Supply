package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IProducts
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.RetrofitClient
import com.google.android.gms.common.api.Response

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
    suspend fun getProductsByPagination(categoryId: Int, page: Int, limit: Int) = retrofit.getProductsByPagination(categoryId, page, limit)


    suspend fun getProductDetails(
        productId: Int,
    ) = retrofit.getProductDetails(
        productId,
    )

    suspend fun addProductToFavorites(token: String, productId: Int, add: Boolean)
    = retrofit.addFavoriteProduct(token, productId,add)

    suspend fun deleteProductToFavorites(token: String, productId: Int, add: Boolean)
            = retrofit.deleteFromFav(token, productId,add)

}