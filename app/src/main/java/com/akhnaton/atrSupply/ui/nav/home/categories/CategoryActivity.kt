package com.akhnaton.atrSupply.ui.nav.home.categories

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrSupply.R
import com.akhnaton.atrSupply.data.model.CategoriesModel
import com.akhnaton.atrSupply.data.statuesValue.nav.home.CategoriesIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.CategoryState
import com.akhnaton.atrSupply.databinding.ActivityCategoryBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.ui.nav.home.product.ProductsActivity
import kotlinx.coroutines.launch


class CategoryActivity : BaseActivity(),
    AllCategoryAdapter.OnCategoryClickListener, View.OnClickListener {

    private lateinit var binding: ActivityCategoryBinding
    private val categoryViewModel: CategoryListViewModel by viewModels()
    private var mAdapter: AllCategoryAdapter = AllCategoryAdapter()
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        category = intent.getStringExtra("category_type").toString()

        setupBinding()
        observeCategory()
        categoryViewModel.handleIntent(CategoriesIntent.GetCategories(category))
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.recCategory.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                this@CategoryActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            itemAnimator = DefaultItemAnimator()
        }
        binding.btnBack.setOnClickListener(this)
    }

    private fun setAdapterData(data: List<CategoriesModel>) {
        mAdapter.setCategoriesList(data, this)
    }

    override fun onCategoryClick(category: CategoriesModel) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra("flag", this.category)
        intent.putExtra("categoryId", category.ID)

        startActivity(intent)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }

    private fun observeCategory() {
        lifecycleScope.launch {
            categoryViewModel.state.collect { state ->
                when (state) {
                    is CategoryState.Idle -> {

                    }
                    is CategoryState.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }
                    is CategoryState.Success -> {
                        hideProgressDialog(binding.progressLoading)
                        setAdapterData(state.response.data!!)
                    }
                    is CategoryState.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.message, true)
                    }
                }
            }
        }
    }
}
