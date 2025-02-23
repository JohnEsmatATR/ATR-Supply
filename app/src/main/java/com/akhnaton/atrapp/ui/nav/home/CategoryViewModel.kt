package com.akhnaton.atrapp.ui.nav.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.ListCategoryModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    val homeIntent = Channel<CategoryIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CategoryStatus>(CategoryStatus.Idle)

    val state: StateFlow<CategoryStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            homeIntent.consumeAsFlow().collect {
                when (it) {
                    is CategoryIntent.GetCategories -> getCategoriesRepo()
                }
            }
        }
    }

    private fun getCategoriesRepo() {
        viewModelScope.launch {
            _state.value = CategoryStatus.Loading
            _state.value = try {
                val response = HomeRepository().getCategory()
                if (response.code() == 200) {
                    CategoryStatus.GetCategory(response.body()!!)
                } else {
                    CategoryStatus.Error(response.message())
                }
            } catch (e: Exception) {
                CategoryStatus.Error(e.message)
            }
        }
    }

}

