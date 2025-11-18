package com.akhnaton.atrSupply.ui.nav.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrSupply.domain.HomeRepository
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
                    is FavoriteIntent.GetFavorite -> getFavorites()
                    is FavoriteIntent.AddProductToFavourites -> addProductToFavorites(
                        it.token,
                        it.productId,
                        it.add,
                        it.categories
                    )

                    is FavoriteIntent.DeleteFromFavourites -> DeleteProductToFavorites(
                        it.token,
                        it.productId,
                        it.add,
                        it.categories
                    )
                }
            }
        }
    }


    private fun getFavorites() {
        viewModelScope.launch {
            _state.value = FavoriteStatus.Loading
            _state.value = try {
                val response = HomeRepository().getFavorite()
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

    private fun addProductToFavorites(token: String, productId: Int, add: Boolean,categories: String) {
        viewModelScope.launch {
            _state.value = FavoriteStatus.Loading
            try {
                val response = HomeRepository().addProductToFavorites(token, productId, add,categories)
                if (response.code() == 200) {
                    _state.value = FavoriteStatus.AddProductToFavourites(response.body()!!)
                } else {
                    _state.value = FavoriteStatus.Error(response.body()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = FavoriteStatus.Error(e.message ?: "Exception occurred")
            }
        }
    }


    private fun DeleteProductToFavorites(token: String, productId: Int, add: Boolean,categories: String) {
        viewModelScope.launch {
            _state.value = FavoriteStatus.Loading
            try {
                val response = HomeRepository().deleteProductToFavorites(token, productId, add,categories)
                if (response.code() == 200) {
                    _state.value =
                        FavoriteStatus.DeleteProductToFavourites(response.body()!!, productId)
                } else {
                    _state.value = FavoriteStatus.Error(response.body()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = FavoriteStatus.Error(e.message ?: "Exception occurred")
            }
        }
    }


}