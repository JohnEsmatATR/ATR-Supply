package com.akhnaton.atrapp.data.interfaces.cart

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface ICheckout {

    @POST(ConstantLinks.GET_MY_CART)
    suspend fun getMyCart(): Response<BaseModel<List<ProductModel>>>

}