package com.akhnaton.atrapp.ui.nav.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

//    val homeIntent = Channel<HomeIntent>(Channel.UNLIMITED)
//
//    private val _state = MutableStateFlow<HomeStatus>(HomeStatus.Idle)
//
//    val state: StateFlow<HomeStatus> get() = _state
//
//    init {
//        makeHomeObserve()
//    }

//    private fun makeHomeObserve() {
//        viewModelScope.launch {
//            homeIntent.consumeAsFlow().collect {
//                when (it) {
//                    is HomeIntent.GetCategories -> getCategoriesRepo(
//                        it.token!!,
//                    )
//
//                    is HomeIntent.GetBrands -> getBrandsRepo(
//                        it.token!!,
//                    )
//
//                    is HomeIntent.GetSubBrands ->  getSubBrandsRepo(
//                        it.token!!,
//                        it.brandId!!,
//                    )
//
//                    is HomeIntent.GetProductsBestSeller -> getProductBestsellerRepo(
//                        it.token!!,
//                        it.limit!!,
//                    )
//
//                }
//            }
//        }
//    }
//
//    private fun getCategoriesRepo(
//        token: String,
//    ) {
//        viewModelScope.launch {
//            _state.value = HomeStatus.Loading
//            _state.value = try {
//                val response = HomeRepository().getCategories(token)
//                if (response.code() == 200) {
//                    HomeStatus.GetCategories(response.body()!!)
//                } else if (response.code() == 401){
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<CategoriesModel, List<String>>>(response.errorBody()?.string(), object :
//                        TypeToken<BaseModel<CategoriesModel, List<String>>>(){}.type)
//                    HomeStatus.GetCategories(theList)
//                } else {
//                    HomeStatus.Error(response.message())
//                }
//            } catch (e: Exception) {
//                HomeStatus.Error(e.message)
//            }
//        }
//    }
//
//    private fun getBrandsRepo(
//        token: String,
//    ) {
//        viewModelScope.launch {
//            _state.value = HomeStatus.Loading
//            _state.value = try {
//                val response = HomeRepository().getBrands(token)
//                if (response.code() == 200) {
//                    HomeStatus.GetBrands(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<BrandsModel, List<String>>>(response.errorBody()?.string(), object :
//                        TypeToken<BaseModel<BrandsModel, List<String>>>(){}.type)
//                    HomeStatus.GetBrands(theList)
//                }
//
//            } catch (e: Exception) {
//                HomeStatus.Error(e.message)
//            }
//
//        }
//    }
//
//    private fun getSubBrandsRepo(
//        token: String,
//        brandId: Int,
//    ) {
//        viewModelScope.launch {
//            _state.value = HomeStatus.Loading
//            _state.value = try {
//                val response = HomeRepository().getSubBrands(token, brandId)
//                if (response.code() == 200) {
//                    HomeStatus.GetSubBrands(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<BrandsModel, List<String>>>(response.errorBody()?.string(), object :
//                        TypeToken<BaseModel<BrandsModel, List<String>>>(){}.type)
//                    HomeStatus.GetSubBrands(theList)
//                }
//
//            } catch (e: Exception) {
//                HomeStatus.Error(e.message)
//            }
//
//        }
//    }
//
//    private fun getProductBestsellerRepo(
//        token: String,
//        limit: Int,
//    ) {
//        viewModelScope.launch {
//            _state.value = HomeStatus.Loading
//            _state.value = try {
//                val response = HomeRepository().getProductsBestSeller(
//                    token,
//                    limit,
//                )
//                if (response.code() == 200) {
//                    HomeStatus.GetProductsBestSeller(response.body()!!)
//                } else {
//                    val error = GsonBuilder().create()
//                    val theList = error.fromJson<BaseModel<ProductsModel, List<String>>>(response.errorBody()?.string(), object :
//                        TypeToken<BaseModel<ProductsModel, List<String>>>(){}.type)
//                    HomeStatus.GetProductsBestSeller(theList)
//                }
//            } catch (e: Exception) {
//                HomeStatus.Error(e.message)
//            }
//        }
//    }

}

