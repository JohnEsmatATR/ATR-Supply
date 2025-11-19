package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.panner.IPanner
import com.akhnaton.atrapp.shared.RetrofitClient

class PannerRepository {
    private val retrofit = RetrofitClient.getInstance(IPanner::class.java)
    suspend fun getPanner() = retrofit.getPanner()
}