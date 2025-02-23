package com.akhnaton.atrapp.ui.nav.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.domain.HomeRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class BestSellerViewModel : ViewModel() {

    val homeIntent = Channel<BestSellerIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<BestSellerStatus>(BestSellerStatus.Idle)

    val state: StateFlow<BestSellerStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            homeIntent.consumeAsFlow().collect {
                when (it) {
                    is BestSellerIntent.GetBestSeller -> getBestsellerRepo()

                }
            }
        }
    }

    private fun getBestsellerRepo() {
        viewModelScope.launch {
            _state.value = BestSellerStatus.Loading
            _state.value = try {
                val response = HomeRepository().getBestSeller()
                if (response.code() == 200) {
                    BestSellerStatus.GetBestSeller(response.body()!!)
                } else {
                    BestSellerStatus.Error(response.body()!!.message)
                }
            } catch (e: Exception) {
                BestSellerStatus.Error(e.message)
            }
        }
    }

}

