package com.akhnaton.atrSupply.data.interfaces.panner

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.panner.PannerResponse
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.http.POST

interface IPanner {



    @POST(ConstantLinks.PANNER)
    suspend fun getPanner(): BaseModel<PannerResponse>
}