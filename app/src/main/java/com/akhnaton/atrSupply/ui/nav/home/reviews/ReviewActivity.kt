package com.akhnaton.atrSupply.ui.nav.home.reviews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrSupply.data.model.ProductModel
import com.akhnaton.atrSupply.data.model.review.ReviewModel
import com.akhnaton.atrSupply.data.statuesValue.nav.home.reviews.ReviewsIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.reviews.ReviewsStatus
import com.akhnaton.atrSupply.databinding.ActivityReviewBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import kotlinx.coroutines.launch

class ReviewActivity : BaseActivity() {
    lateinit var binding: ActivityReviewBinding
    lateinit var reviewAdapter: ReviewDetailsAdapter
    private val reviewsViewModel: ReviewsViewModel by viewModels()
    val listReviews = ArrayList<ReviewModel>()
    var product = ProductModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    override fun onResume() {
        super.onResume()
        getReviews()
    }

    private fun init() {
        product = intent.getSerializableExtra("product") as ProductModel
        reviewsObserve()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnGiveAReview.setOnClickListener {
            val intent = Intent(this@ReviewActivity, AddReviewActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
    }

    private fun setupReviewRecycler(list: List<ReviewModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        reviewAdapter = ReviewDetailsAdapter(
            onClick = { product, position -> },
        )
        reviewAdapter.setData(list)
        binding.recyclerReviews.layoutManager = layoutManager
        binding.recyclerReviews.adapter = reviewAdapter
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

                    is ReviewsStatus.GetReviews -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            if (it.data.data!!.isNotEmpty()) {
                                binding.txtNoReviews.visibility = View.GONE
                                listReviews.clear()
                                listReviews.addAll(it.data.data!!)
                                setupReviewRecycler(listReviews)
                                binding.simpleRatingBar.rating = getTotalReviews(it.data.data!!)
                                binding.txtRating.text = getTotalReviews(it.data.data!!).toString()
                            } else {
                                binding.txtNoReviews.visibility = View.VISIBLE
                            }
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is ReviewsStatus.AddReview -> {}

                    is ReviewsStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun getReviews() {
        lifecycleScope.launch {
            reviewsViewModel.reviewIntent.send(
                ReviewsIntent.GetReviews(product.ID.toString())
            )
        }
    }

    private fun getTotalReviews(list: List<ReviewModel>): Float {
        var total = 0.0f
        for (review in list) {
            total += review.RATE.toFloat()
        }
        val average = total / list.size.toFloat()
        return average
    }
}