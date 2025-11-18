package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.GetCategories
import com.akhnaton.atrSupply.shared.RetrofitClient

class CategoriesRepository {

    private val retrofit = RetrofitClient.getInstance(GetCategories::class.java)

    suspend fun getCategories() = retrofit.getCategories()
}

