package com.akhnaton.atrapp.ui.nav.home.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsStatus
import com.akhnaton.atrapp.domain.HomeRepository
import com.akhnaton.atrapp.domain.ReviewsRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ReviewsViewModel : ViewModel() {

    val reviewIntent = Channel<ReviewsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ReviewsStatus>(ReviewsStatus.Idle)

    val state: StateFlow<ReviewsStatus> get() = _state

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            reviewIntent.consumeAsFlow().collect {
                when (it) {
                    is ReviewsIntent.GetReviews -> getReviews(
                        it.version,
                        it.productId,
                    )

                    is ReviewsIntent.AddReview -> addReviews(
                        it.version,
                        it.itemId,
                        it.reviewComment,
                        it.reviewValue,
                    )
                }
            }
        }
    }


    private fun getReviews(
        version: String,
        productId: String,
    ) {
        viewModelScope.launch {
            _state.value = ReviewsStatus.Loading
            _state.value = try {
                val response = ReviewsRepository().getReviews(version, productId)
                if (response.code() == 200) {
                    ReviewsStatus.GetReviews(response.body()!!)
                } else {
                    ReviewsStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                ReviewsStatus.Error(e.message)
            }

        }
    }

    private fun addReviews(
        version: String,
        itemId: String,
        reviewComment: String,
        reviewValue: String,
    ) {
        viewModelScope.launch {
            _state.value = ReviewsStatus.Loading
            _state.value = try {
                val response = ReviewsRepository().addReview(
                    version,
                    itemId,
                    reviewComment,
                    reviewValue,
                )
                if (response.code() == 200) {
                    ReviewsStatus.AddReview(response.body()!!)
                } else {
                    ReviewsStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                ReviewsStatus.Error(e.message)
            }

        }
    }

}

