package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface IProducts {

    @POST(ConstantLinks.GET_CATEGORY_LIST)
    @FormUrlEncoded
    suspend fun getCategories(
        @Field("parent_category") categories: String
    ): Response<BaseModel<List<CategoriesModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getBestSeller(
        @Field("best_seller") bestSeller: Int?,
        @Field("order_type") orderType: String?,
    ): Response<BaseModel<List<ProductModel>>>


    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getProducts(
        @Field("category_id") categoryId: Int?,
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getFavorite(
        @Field("liked") liked: Int?,
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun searchProduct(
        @Field("search") search: String? = "",
        @Field("order_type") categoryId: String,
        @Field("category_id") categories: Int,
        @Field("page") page: Int,
        @Field("per_page") limit: Int
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getProductDetails(
        @Field("product_id") productId: Int?,
        @Field("order_type") categories: String
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_FAV_PRODUCT)
    suspend fun addFavoriteProduct(
        @Header("Authorization") token: String,
        @Field("item_id") productId: Int,
        @Field("liked") liked: Boolean,
        @Field("order_type") categories: String
    ): Response<BaseModel<Any>>

    @FormUrlEncoded
    @POST(ConstantLinks.DELETE_FROM_FAV)
    suspend fun deleteFromFav(
        @Header("Authorization") token: String,
        @Field("item_id") productId: Int,
        @Field("liked") liked: Boolean,
        @Field("order_type") categories: String
    ): Response<BaseModel<Any>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun getProductsByPagination(
        @Field("category_id") categoryId: Int?,
        @Field("page") page: Int,
        @Field("per_page") limit: Int,
        @Field("order_type") categories: String
    ): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.GET_ALL_PRODUCT)
    suspend fun filterProduct(
        @Field("search") search: String? = "",
        @Field("category_id") categoryId: Int
    ): Response<BaseModel<List<ProductModel>>>

}