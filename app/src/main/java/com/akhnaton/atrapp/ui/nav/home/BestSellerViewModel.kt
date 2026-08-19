package com.akhnaton.atrapp.ui.nav.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.akhnaton.atrapp.shared.Common
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
                    is BestSellerIntent.GetBestSeller -> getBestsellerRepo(1, it.orderType)

                }
            }
        }
    }

    private fun getBestsellerRepo(bestSeller: Int, orderType: String) {
        viewModelScope.launch {
            _state.value = BestSellerStatus.Loading
            _state.value = try {
                val response = HomeRepository().getBestSeller(bestSeller, orderType)
                if (response.code() == 200) {
                    Log.d(Common.KeroDebug, "getBestsellerRepo ${response.body()!!}")
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

