package com.akhnaton.atrapp.ui.nav.home.categories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.databinding.ActivityCategoryBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.CategoryViewModel
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity
import kotlinx.coroutines.launch


class CategoryActivity : BaseActivity(), AllCategoryAdapter.OnCategoryClickListener,
    View.OnClickListener {
    private lateinit var binding: ActivityCategoryBinding
    private var listCategory = mutableListOf<CategoryModel>()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var mAdapter: AllCategoryAdapter = AllCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        categoryObserve()
        getCategories()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.recCategory.adapter = mAdapter
        binding.recCategory.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recCategory.itemAnimator = DefaultItemAnimator()
        binding.btnBack.setOnClickListener(this)
    }

    private fun setAdapterData(data: List<CategoryModel>) {
        mAdapter.setCategoriesList(data, this)
    }

    override fun onCategoryClick(category: CategoryModel) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra("flag", Common.category)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }

    private fun categoryObserve() {
        lifecycleScope.launch {
            categoryViewModel.state.collect {
                when (it) {
                    is CategoryStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is CategoryStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is CategoryStatus.GetCategory -> {
                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetCategories")
//                            listCategory.addAll(it.data.data!!)
//                            Log.d(Common.KeroDebug, "observeHome: GetCategoriessssssss${listCategory.size}")
//                            setAdapterData(listCategory)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is CategoryStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun getCategories() {
//        lifecycleScope.launch {
//            categoryViewModel.homeIntent.send(
//                CategoryIntent.GetCategories
//            )
//        }
    }
}