package com.akhnaton.atrapp.ui.nav.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchStatus
import com.akhnaton.atrapp.domain.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val searchIntent = Channel<SearchIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SearchStatus>(SearchStatus.Idle)

    val state: StateFlow<SearchStatus> get() = _state

//    var currentPage = 1
    private val limit = 10

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            searchIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchIntent.SearchProduct -> searchProduct(
                        it.word,
                        it.categoryId,
                        it.categories,
                        it.page
                    )

                }
            }
        }
    }


    private fun searchProduct(
        word: String?,
        categoryId: String,
        categories: Int,
        page: Int
    ) {
        viewModelScope.launch {
            _state.value = SearchStatus.Loading
            try {
                val response = HomeRepository().searchProduct(
                    word,
                    categoryId,
                    categories,
                    page,
                    limit
                )
                _state.value = SearchStatus.SearchProduct(response.body()!!)
            } catch (e: Exception) {
                _state.value = SearchStatus.Error(e.message)
            }
        }
    }

}