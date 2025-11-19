package com.akhnaton.atrapp.data.statuesValue.nav.panner

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.panner.PannerResponse

sealed class PannerState {
    data object Loading : PannerState()

    data class Success(val data: BaseModel<PannerResponse>) : PannerState()


    data class Error(val message: String) : PannerState()
}
