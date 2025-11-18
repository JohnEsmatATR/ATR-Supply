package com.akhnaton.atrSupply.ui.nav.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.home.search.SearchIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.search.SearchStatus
import com.akhnaton.atrSupply.domain.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val searchIntent = Channel<SearchIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SearchStatus>(SearchStatus.Idle)

    val state: StateFlow<SearchStatus> get() = _state

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            searchIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchIntent.SearchProduct -> searchProduct(
                        it.word,it.categoryId , it.categories
                    )


                }
            }
        }
    }


    private fun searchProduct(
        word:String?="",
        categoryId : String,
        categories: Int
    ) {
        viewModelScope.launch {
            _state.value = SearchStatus.Loading
            _state.value = try {
                val response = HomeRepository().searchProduct(word,categoryId, categories)
                if (response.code() == 200) {
                    SearchStatus.SearchProduct(response.body()!!)
                } else {
                    SearchStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                SearchStatus.Error(e.message)
            }

        }
    }

}