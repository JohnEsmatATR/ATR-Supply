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

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            searchIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchIntent.SearchProduct -> searchProduct(
                        it.version,
                        it.word,
                    )
                }
            }
        }
    }


    private fun searchProduct(
        version:String,
        word:String,
    ) {
        viewModelScope.launch {
            _state.value = SearchStatus.Loading
            _state.value = try {
                val response = HomeRepository().searchProduct(version, word)
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

