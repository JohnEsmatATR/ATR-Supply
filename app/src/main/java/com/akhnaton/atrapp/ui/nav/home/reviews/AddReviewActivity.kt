package com.akhnaton.atrapp.ui.nav.home.reviews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.reviews.ReviewsStatus
import com.akhnaton.atrapp.databinding.ActivityAddReviewBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import kotlinx.coroutines.launch

class AddReviewActivity : BaseActivity() {
    lateinit var binding: ActivityAddReviewBinding
    private val reviewsViewModel: ReviewsViewModel by viewModels()
    var product = ProductModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        product = intent.getSerializableExtra("product") as ProductModel
        reviewsObserve()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnSubmitReview.setOnClickListener {
            if (isVerify()) {
                val comment = binding.txtAddReview.text.toString()
                addReview(comment, binding.simpleRatingBar.rating.toString())
            }
        }
    }

    private fun reviewsObserve() {
        lifecycleScope.launch {
            reviewsViewModel.state.collect {
                when (it) {
                    is ReviewsStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is ReviewsStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is ReviewsStatus.GetReviews -> {}

                    is ReviewsStatus.AddReview -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)

                            showToastSnack(it.data.message, false)
                            finish()
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is ReviewsStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }


    private fun addReview(
        reviewComment: String,
        reviewValue: String,
    ) {
        lifecycleScope.launch {
            reviewsViewModel.reviewIntent.send(
                ReviewsIntent.AddReview(
                    getVersion(),
                    product.ID.toString(),
                    reviewComment,
                    reviewValue,
                )
            )
        }
    }


    private fun isVerify(): Boolean {
        val comment = binding.txtAddReview.text.toString()
        if (comment.isEmpty()) {
            showToastSnack("Please Add Review First" ,true)
            return false
        } else {
            return true
        }
    }

}