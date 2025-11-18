package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.panner.IPanner
import com.akhnaton.atrSupply.shared.RetrofitClient

class PannerRepository {
    private val retrofit = RetrofitClient.getInstance(IPanner::class.java)
    suspend fun getPanner() = retrofit.getPanner()
}