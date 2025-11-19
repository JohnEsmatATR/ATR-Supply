package com.akhnaton.atrapp.ui.nav.home.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsStatus
import com.akhnaton.atrapp.domain.ReviewsRepository
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
                        it.productId,
                    )

                    is ReviewsIntent.AddReview -> addReviews(
                        it.itemId,
                        it.reviewComment,
                        it.reviewValue,
                    )
                }
            }
        }
    }


    private fun getReviews(
        productId: String,
    ) {
        viewModelScope.launch {
            _state.value = ReviewsStatus.Loading
            _state.value = try {
                val response = ReviewsRepository().getReviews(productId)
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
        itemId: String,
        reviewComment: String,
        reviewValue: String,
    ) {
        viewModelScope.launch {
            _state.value = ReviewsStatus.Loading
            _state.value = try {
                val response = ReviewsRepository().addReview(
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

