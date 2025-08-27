package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.GetCategories
import com.akhnaton.atrapp.shared.RetrofitClient

class CategoriesRepository {

    private val retrofit = RetrofitClient.getInstance(GetCategories::class.java)

    suspend fun getCategories() = retrofit.getCategories()
}

