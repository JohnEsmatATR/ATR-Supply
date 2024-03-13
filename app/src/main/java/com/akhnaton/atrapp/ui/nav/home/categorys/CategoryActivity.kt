package com.akhnaton.atrapp.ui.nav.home.categorys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private var mList = mutableListOf<CategoryModel>()
    private var mAdapter: AllCategoryAdapter = AllCategoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.recCategory.adapter = mAdapter
        binding.recCategory.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            val decoration =
                DividerItemDecoration(this@CategoryActivity, LinearLayoutManager.VERTICAL)
            addItemDecoration(decoration)
        }
        fillList()
    }


    private fun fillList(){
        val c1 = CategoryModel(0,"Skin Care","Skin Care","","","","")
        val c2 = CategoryModel(0,"Skin Care","Skin Care","","","","")
        val c3 = CategoryModel(0,"Skin Care","Skin Care","","","","")
        val c4 = CategoryModel(0,"Skin Care","Skin Care","","","","")
        mList.add(c1)
        mList.add(c2)
        mList.add(c3)
        mList.add(c4)
        setAdapterData(mList)
    }

    private fun setAdapterData(data: List<CategoryModel>) {
        mAdapter.setCategoriesList(data)
    }
}