package com.akhnaton.atrapp.ui.nav.home.reviews

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ReviewModel
import com.akhnaton.atrapp.databinding.ActivityReviewBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ReviewAdapter

class ReviewActivity : BaseActivity() {
    lateinit var binding: ActivityReviewBinding
    lateinit var reviewAdapter: ReviewDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

        val list = ArrayList<ReviewModel>()
        list.add(ReviewModel("Belal", 4.2f, "22/02/2024", "I bought it 3 weeks ago and now come back just to say “Awesome Product”. I really enjoy it. At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupt et quas molestias excepturi sint non provident.",R.drawable.test_profile))
        list.add(ReviewModel("Belal", 4.2f, "22/02/2024", "I bought it 3 weeks ago and now come back just to say “Awesome Product”. I really enjoy it. At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupt et quas molestias excepturi sint non provident.",R.drawable.test_profile))
        list.add(ReviewModel("Belal", 4.2f, "22/02/2024", "I bought it 3 weeks ago and now come back just to say “Awesome Product”. I really enjoy it. At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupt et quas molestias excepturi sint non provident.",R.drawable.test_profile))
        list.add(ReviewModel("Belal", 4.2f, "22/02/2024", "I bought it 3 weeks ago and now come back just to say “Awesome Product”. I really enjoy it. At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupt et quas molestias excepturi sint non provident.",R.drawable.test_profile))
        list.add(ReviewModel("Belal", 4.2f, "22/02/2024", "I bought it 3 weeks ago and now come back just to say “Awesome Product”. I really enjoy it. At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupt et quas molestias excepturi sint non provident.",R.drawable.test_profile))

        setupReviewRecycler(list)

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnGiveAReview.setOnClickListener {
            val intent = Intent(this@ReviewActivity, AddReviewActivity::class.java)
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


}