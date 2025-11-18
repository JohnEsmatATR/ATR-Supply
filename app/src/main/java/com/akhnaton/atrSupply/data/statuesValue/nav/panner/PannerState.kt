package com.akhnaton.atrSupply.data.statuesValue.nav.panner

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.panner.PannerResponse

sealed class PannerState {
    data object Loading : PannerState()

    data class Success(val data: BaseModel<PannerResponse>) : PannerState()


    data class Error(val message: String) : PannerState()
}
