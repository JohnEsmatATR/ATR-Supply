package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.ReviewModel
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    lateinit var productSuggestAdapter: ProductAdapter
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()

    }

    private fun init() {
        binding.txtOldPrice.paintFlags = binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val list = ArrayList<ProductModel>()
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))

        setupProductSuggestRecycler(list)

        val list2 = ArrayList<ReviewModel>()
        list2.add(ReviewModel("Belal", 4.2f, "22/02/2024", "هذا المنتج جميل مقابل سعره"))
        list2.add(ReviewModel("Belal", 4.2f, "22/02/2024", "هذا المنتج جميل مقابل سعره"))

        setupReviewRecycler(list2)

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupProductSuggestRecycler(list: List<ProductModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        productSuggestAdapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(baseContext, ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("id", product.id)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
//                addProductToFavorite(product.id, isFavorite)
            }
        )
        productSuggestAdapter.setData(list, true, Common.bestSeller)
        binding.recyclerSuggest.layoutManager = layoutManager
        binding.recyclerSuggest.adapter = productSuggestAdapter
    }

    private fun setupReviewRecycler(list: List<ReviewModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        reviewAdapter = ReviewAdapter(
            onClick = { product, position -> },
        )
        reviewAdapter.setData(list)
        binding.recyclerReviews.layoutManager = layoutManager
        binding.recyclerReviews.adapter = reviewAdapter
    }

}