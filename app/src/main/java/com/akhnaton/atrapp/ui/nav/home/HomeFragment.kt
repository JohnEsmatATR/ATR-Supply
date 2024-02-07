package com.akhnaton.atrapp.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.FragmentHomeBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity


class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    //    private val favoriteViewModel: FavoriteViewModel by viewModels()
    lateinit var categoriesAdapter: CategoryAdapter
    lateinit var bestSellerAdapter: ProductAdapter
//    lateinit var categoriesModel: List<CategoryModel>
//    lateinit var brandsModel: List<BrandModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        observeLogin()
        onClick()


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        init()
    }

    private fun observeLogin() {
//        lifecycleScope.launch {
//            viewModel.state.collect {
//                when (it) {
//                    is HomeStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is HomeStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is HomeStatus.GetCategories -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetCategories")
//                            categoriesModel = it.data.data!!.categories
//
//                            setupCategoriesRecycler(categoriesModel)
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is HomeStatus.GetBrands -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetBrands")
//                            brandsModel = it.data.data!!.brands
//
//                            setupBrandsRecycler(brandsModel)
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is HomeStatus.GetSubBrands -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetSubBrands")
//                            brandsModel = it.data.data!!.brands
//
//                            setupBrandsRecycler(brandsModel)
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.errors!![0], true)
//                        }
//
//                    }
//
//                    is HomeStatus.GetProductsBestSeller -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//
//                            setupProductBestSellerRecycler(it.data.data!!.products)
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is HomeStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
    }

    private fun favoriteObserve() {
//        lifecycleScope.launch {
//            favoriteViewModel.state.collect {
//                when (it) {
//                    is FavoriteStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is FavoriteStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is FavoriteStatus.AddProductToFavourites -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            showToastSnack(it.data.message, false)
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is FavoriteStatus.GetMyFavourites -> {}
//
//                    is FavoriteStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//                }
//            }
//        }
    }

    private fun addProductToFavorite(
        productId: Int,
        add: Boolean,
    ) {
//        lifecycleScope.launch {
//            favoriteViewModel.favoriteIntent.send(
//                FavoriteIntent.AddProductToFavourites(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                    add,
//                )
//            )
//        }
    }

    private fun getCategories() {
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                HomeIntent.GetCategories(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                )
//            )
//        }
    }

    private fun getBrands() {
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                HomeIntent.GetBrands(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                )
//            )
//        }
    }

    private fun getProductsBestSeller() {
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                HomeIntent.GetProductsBestSeller(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    5
//                )
//            )
//        }
    }

    private fun setupCategoriesRecycler(list: List<CategoryModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriesAdapter = CategoryAdapter(onClick = { category, position ->
//            val intent = Intent(requireContext(), ProductsActivity::class.java)
//            intent.putExtra("flag", Common.category)
//            intent.putExtra("id", category.id)
//            startActivity(intent)
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
                val intent = Intent(requireContext(), ProductsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("id", product.id)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
                addProductToFavorite(product.id, isFavorite)
            }
        )
        bestSellerAdapter.setData(list, true, Common.bestSeller)
        binding.recyclerBestSeller.layoutManager = layoutManager
        binding.recyclerBestSeller.adapter = bestSellerAdapter
    }


    private fun init() {
//        binding.progressLoading.isEnabled = false
////        binding.txtSearch.hint = "Search"
//
//        if (SharedPreferenceHelper.membership == 0) {
//            binding.layoutStartABusiness.visibility = View.VISIBLE
//        } else {
//            binding.layoutStartABusiness.visibility = View.GONE
//        }
//
//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
//        categoriesAdapter = CategoriesAdapter(onClick = { category, position ->
//
//        })
//
//        getCategories()
//        getBrands()
//        getProductsBestSeller()
//        favoriteObserve()
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
//        binding.txtSeeAllBestSeller.setOnClickListener {
//            val intent = Intent(context, ProductsActivity::class.java)
//            intent.putExtra("flag", Common.bestSeller)
//            startActivity(intent)
//        }
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
//        binding.btnStartBusiness.setOnClickListener {
//            val intent = Intent(context, StartBusinessActivity::class.java)
//            startActivity(intent)
//        }

        val list = ArrayList<ProductModel>()
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))

        setupProductBestSellerRecycler(list)

        val list_ = ArrayList<CategoryModel>()
        list_.add(CategoryModel(0,"mmmmm","","","","",""))
        list_.add(CategoryModel(0,"mmmmm","","","","",""))
        list_.add(CategoryModel(0,"mmmmm","","","","",""))
        list_.add(CategoryModel(0,"mmmmm","","","","",""))
        list_.add(CategoryModel(0,"mmmmm","","","","",""))

        setupCategoriesRecycler(list_)

    }
}