package com.akhnaton.atrapp.data.interfaces.panner

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.panner.PannerResponse
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.http.POST

interface IPanner {



    @POST(ConstantLinks.PANNER)
    suspend fun getPanner(): BaseModel<PannerResponse>
}