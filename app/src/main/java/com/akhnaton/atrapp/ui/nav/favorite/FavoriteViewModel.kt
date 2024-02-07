package com.akhnaton.atrapp.ui.nav.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

//    val favoriteIntent = Channel<FavoriteIntent>(Channel.UNLIMITED)
//
//    private val _state = MutableStateFlow<FavoriteStatus>(FavoriteStatus.Idle)
//
//    val state: StateFlow<FavoriteStatus> get() = _state
//
//    init {
//        observe()
//    }
//
//    private fun observe() {
//        viewModelScope.launch {
//            favoriteIntent.consumeAsFlow().collect {
//                when (it) {
//                    is FavoriteIntent.AddProductToFavourites -> addProductToFavourites(
//                        it.token!!,
//                        it.productId!!,
//                        it.add!!,
//                    )
//
//                    is FavoriteIntent.GetMyFavourites -> getMyFavourites(
//                        it.token!!,
//                    )
//
//
//                }
//            }
//        }
//    }
//
//
//    private fun addProductToFavourites(
//        token: String?,
//        productId: Int?,
//        add: Boolean?,
//    ) {
//        viewModelScope.launch {
//            _state.value = FavoriteStatus.Loading
//            _state.value = try {
//                val response = FavoriteRepository().addProductToFavorite(
//                    token,
//                    productId,
//                    add,
//                )
//                if (response.code() == 200) {
//                    FavoriteStatus.AddProductToFavourites(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<OneProductModel, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<OneProductModel, List<String>>>() {}.type
//                    )
//                    FavoriteStatus.AddProductToFavourites(theList)
//                }
//
//            } catch (e: Exception) {
//                FavoriteStatus.Error(e.message)
//            }
//
//        }
//    }
//
//
//    private fun getMyFavourites(
//        token: String,
//    ) {
//        viewModelScope.launch {
//            _state.value = FavoriteStatus.Loading
//            _state.value = try {
//                val response = FavoriteRepository().getMyFavorite(token)
//                if (response.code() == 200) {
//                    FavoriteStatus.GetMyFavourites(response.body()!!)
//                } else if (response.code() == 401) {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<List<ProductModel>, List<String>>>(
//                        response.errorBody()?.string(), object :
//                            TypeToken<BaseModel<List<ProductModel>, List<String>>>() {}.type
//                    )
//                    FavoriteStatus.GetMyFavourites(theList)
//                } else {
//                    FavoriteStatus.Error(response.message())
//                }
//            } catch (e: Exception) {
//                FavoriteStatus.Error(e.message)
//            }
//        }
//    }

}

