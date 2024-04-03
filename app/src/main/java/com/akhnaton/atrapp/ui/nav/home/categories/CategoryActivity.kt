package com.akhnaton.atrapp.ui.nav.home.categories

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.databinding.ActivityCategoryBinding
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity


class CategoryActivity : AppCompatActivity(), AllCategoryAdapter.OnCategoryClickListener,
    View.OnClickListener {
    private lateinit var binding: ActivityCategoryBinding
    private var mList = mutableListOf<CategoryModel>()
    private var mAdapter: AllCategoryAdapter = AllCategoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
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
        fillList()
    }


    private fun fillList() {
        val c1 = CategoryModel(0, "Skin Care", "Skin Care", "", "", "", "")
        val c2 = CategoryModel(0, "Hair Care", "Hair Care", "", "", "", "")
        val c3 = CategoryModel(0, "Sun Care", "Sun Care", "", "", "", "")
        val c4 = CategoryModel(0, "Oral Care", "Oral Care", "", "", "", "")
        mList.add(c1)
        mList.add(c2)
        mList.add(c3)
        mList.add(c4)
        setAdapterData(mList)
    }

    private fun setAdapterData(data: List<CategoryModel>) {
        mAdapter.setCategoriesList(data, this)
    }

    override fun onCategoryClick(category: CategoryModel) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra("flag", Common.category)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }
}