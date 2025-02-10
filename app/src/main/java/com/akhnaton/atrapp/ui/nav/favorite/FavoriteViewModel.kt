package com.akhnaton.atrapp.ui.nav.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    val favoriteIntent = Channel<FavoriteIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<FavoriteStatus>(FavoriteStatus.Idle)

    val state: StateFlow<FavoriteStatus> get() = _state

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            favoriteIntent.consumeAsFlow().collect {
                when (it) {
                    is FavoriteIntent.GetFavorite -> addProductToFavourites(it.version)
                }
            }
        }
    }


    private fun addProductToFavourites(
        version:String,
    ) {
        viewModelScope.launch {
            _state.value = FavoriteStatus.Loading
            _state.value = try {
                val response = HomeRepository().getFavorite(version)
                if (response.code() == 200) {
                    FavoriteStatus.GetFavorite(response.body()!!)
                } else {
                    FavoriteStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                FavoriteStatus.Error(e.message)
            }

        }
    }

}

