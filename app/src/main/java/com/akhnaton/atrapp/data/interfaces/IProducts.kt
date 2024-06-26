package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ListCategoryModel
import com.akhnaton.atrapp.data.model.ListProductModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IProducts {

    @POST(ConstantLinks.GET_CATEGORIES)
    suspend fun getCategories(
        @Header("Authorization") token: String?,
    ): Response<BaseModel<List<CategoryModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getBestSeller(
        @Header("Authorization") token: String?,
        @Field("best_seller") bestSeller: Int?,
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getProducts(
        @Header("Authorization") token: String?,
        @Field("category_id") categoryId: Int?,
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getFavorite(
        @Header("Authorization") token: String?,
        @Field("liked") liked: Int?,
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getProductDetails(
        @Header("Authorization") token: String?,
        @Field("product_id") productId: Int?,
    ): Response<BaseModel<List<ProductModel>>>

}