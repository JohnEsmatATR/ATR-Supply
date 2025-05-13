package com.akhnaton.atrapp.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.databinding.FragmentHomeBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.categories.CategoryActivity
import com.akhnaton.atrapp.ui.nav.home.notifications.NotificationsActivity
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.search.SearchActivity
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()

    lateinit var categoriesAdapter: CategoryAdapter
    lateinit var bestSellerAdapter: ProductAdapter
    var listCategory = mutableListOf<CategoryModel>()
    var listBestSeller = mutableListOf<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)


        onClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        init()
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
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)

                            listCategory.clear()
                            listCategory.addAll(it.data.data!!)
                            Log.d(Common.KeroDebug, "observeHome: GetCategories : ${it.data.data!!}")
                            setupCategoriesRecycler(listCategory)
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

    private fun bestSellerObserve() {
        lifecycleScope.launch {
            bestSellerViewModel.state.collect {
                when (it) {
                    is BestSellerStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is BestSellerStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is BestSellerStatus.GetBestSeller -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            listBestSeller.clear()
                            listBestSeller.addAll(it.data.data!!)
                            listBestSeller.size
                            Log.d(Common.KeroDebug, "observeHome: best saler ${it.data.data!!}")
                            setupProductBestSellerRecycler(listBestSeller)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is BestSellerStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun getCategories() {
        lifecycleScope.launch {
            categoryViewModel.homeIntent.send(
                CategoryIntent.GetCategories
            )
        }
    }

    private fun getBestSeller() {
        lifecycleScope.launch {
            bestSellerViewModel.homeIntent.send(
                BestSellerIntent.GetBestSeller(1)
            )
        }
    }

    private fun setupCategoriesRecycler(list: List<CategoryModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriesAdapter = CategoryAdapter(onClick = { category, position ->
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            intent.putExtra("flag", Common.category)
            intent.putExtra("category", category)
            startActivity(intent)
        })
        categoriesAdapter.setData(list)
        binding.recyclerCategory.layoutManager = layoutManager
        binding.recyclerCategory.adapter = categoriesAdapter
    }

    private fun setupProductBestSellerRecycler(list: List<ProductModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bestSellerAdapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("product", product)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
//                addProductToFavorite(product.id, isFavorite)
            }
        )
        bestSellerAdapter.setData(list, true, Common.bestSeller)
        binding.recyclerBestSeller.layoutManager = layoutManager
        binding.recyclerBestSeller.adapter = bestSellerAdapter
    }


    private fun init() {
        binding.progressLoading.isEnabled = false

        categoryObserve()
        bestSellerObserve()
        getCategories()
        getBestSeller()
    }

    private fun onClick() {
//        binding.txtSeeAllCategory.setOnClickListener {
//            val intent = Intent(context, SeeAllActivity::class.java)
//            intent.putExtra("flag", Common.category)
//            startActivity(intent)
//        }
//        binding.txtSeeAllBrand.setOnClickListener {
//            val intent = Intent(context, SeeAllActivity::class.java)
//            intent.putExtra("flag", Common.brand)
//            startActivity(intent)
//        }
        binding.txtSeeAllBestSeller.setOnClickListener {
            val intent = Intent(context, ProductsActivity::class.java)
            intent.putExtra("flag", Common.bestSeller)
            startActivity(intent)
        }
//        binding.btnBrowseAllBlog.setOnClickListener {
//            val intent = Intent(context, BlogActivity::class.java)
//            startActivity(intent)
//        }
//        binding.btnSearch.setOnClickListener {
//            val txt = binding.txtSearch.query.toString()
//            val intent = Intent(context, SearchActivity::class.java)
//            intent.putExtra(Common.search, txt)
//            startActivity(intent)
//        }
        binding.imNotification.setOnClickListener {
            val intent = Intent(context, NotificationsActivity::class.java)
            startActivity(intent)
        }
        binding.txtSeeAllCategory.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java)
            startActivity(intent)
        }
        binding.cardSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

    }
}