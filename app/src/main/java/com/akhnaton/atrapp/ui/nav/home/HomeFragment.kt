package com.akhnaton.atrapp.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerState
import com.akhnaton.atrapp.databinding.FragmentHomeBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.shared.ShimmerAdapter
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.categories.CategoryActivity
import com.akhnaton.atrapp.ui.nav.home.notifications.NotificationsActivity
import com.akhnaton.atrapp.ui.nav.home.panner.BannerAdapter
import com.akhnaton.atrapp.ui.nav.home.panner.PannerViewModel
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.search.SearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    lateinit var categoriesAdapter: CategoryAdapter
    lateinit var bestSellerAdapter: ProductAdapter
    var listCategory = mutableListOf<CategoryModel>()
    var listBestSeller = mutableListOf<ProductModel>()
    private lateinit var shimmerAdapter: ShimmerAdapter
    private val viewModel: AddressesViewModel by viewModels()

    private val pannerViewModel: PannerViewModel by viewModels()
    private lateinit var viewPager: ViewPager

    private var currentPage = 0
    private var direction = 1
    private var sliderJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        onClick()

        binding.cardAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressesActivity::class.java)
            startActivity(intent)
        }
        init()
        return binding.root

    }

    override fun onResume() {
        super.onResume()

    }

    private fun categoryObserve() {
        lifecycleScope.launch {
            categoryViewModel.state.collect {
                when (it) {
                    is CategoryStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is CategoryStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        binding.recyclerCategory.adapter = shimmerAdapter
                    }

                    is CategoryStatus.GetCategory -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            binding
                            listCategory.clear()
                            listCategory.addAll(it.data.data!!)
                            Log.d(Common.KeroDebug, "observeHome: GetCategories : ${it.data.data!!}")
                            setupPharmaRecycler(listCategory)
                            setupCosmeticsRecycler(listCategory)
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
    private fun observeAddress() {
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.d("DEBUGGGGG", "Received state: $it")
                when (it) {
                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idleeeee")
                    is AddressStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddressStatus.GetMyAddresses -> {
//                        if (it.result.status == 200) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "Received: GetProducts")
//
//                            val addresses = it.result.data ?: emptyList()
//
//
//                            addresses.forEach { address ->
//                                if (address.prime == 1) {
//                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
//                                    binding.defaultAddress.text=address.TITLE
//                                }
//                            }
//
//                            val hasDefault = addresses.any { address -> address.prime == 1 }
//                            if (!hasDefault) {
//                                val intent = Intent(requireContext(), AddressesActivity::class.java)
//                                startActivity(intent)
//                            }
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                        }
                    }



                    is AddressStatus.MakeAddressPrime -> {}

                    is AddressStatus.Error -> {}

                }
            }
        }
    }
    private fun getAddress() {
        lifecycleScope.launch {
            viewModel.addressIntent.send(
                AddressIntent.GetMyAddresses
            )
        }
    }
    private fun bestSellerObserve() {
        lifecycleScope.launch {
            bestSellerViewModel.state.collect {
                when (it) {
                    is BestSellerStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is BestSellerStatus.Loading -> {
                        Log.d(Common.KeroDebug, "bestSellerObserve: Loading")
                      //  showProgressDialog(binding.progressLoading)
                        binding.recyclerBestSeller.adapter = shimmerAdapter
                    }

                    is BestSellerStatus.GetBestSeller -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            listBestSeller.clear()
                            listBestSeller.addAll(it.data.data!!)
                            listBestSeller.size
                            Log.d(Common.KeroDebug, "bestSellerObserve: best saler ${it.data.data!!}")
                            setupProductBestSellerRecycler(listBestSeller)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is BestSellerStatus.Error -> {
                        Log.d(Common.KeroDebug, "bestSellerObserve Error: ${it.error.toString()}")
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
                BestSellerIntent.GetBestSeller(2)
            )
        }
    }

    private fun setupCosmeticsRecycler(list: List<CategoryModel>) {
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
    private fun setupPharmaRecycler(list: List<CategoryModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriesAdapter = CategoryAdapter(onClick = { category, position ->
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            intent.putExtra("flag", Common.category)
            intent.putExtra("category", category)

            startActivity(intent)
        })
        categoriesAdapter.setData(list)
        binding.recyclerPharma.layoutManager = layoutManager
        binding.recyclerPharma.adapter = categoriesAdapter
    }

    private fun setupProductBestSellerRecycler(list: List<ProductModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bestSellerAdapter = ProductAdapter(
            onClick = { product, position, sharedView, transitionName ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java).apply {
                    putExtra("flag", Common.category)
                    putExtra("product", product)
                    putExtra("transitionName", transitionName)
                    putExtra("product_id",product.ID)

                }

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    sharedView,
                    transitionName
                )

                startActivity(intent, options.toBundle())
            },
            onFavoriteClick = { product, position, isFavorite ->
                if (isFavorite) {
                    addProductToFavorite(product.ID, isFavorite)
                } else {
                    deleteProductToFavorite(product.ID, isFavorite)
                }
            }
        )

        bestSellerAdapter.isInHome = true
        bestSellerAdapter.setData(list, true, Common.bestSeller)
        binding.recyclerBestSeller.layoutManager = layoutManager
        binding.recyclerBestSeller.adapter = bestSellerAdapter
    }



    private fun init() {
        binding.progressLoading.isEnabled = false
        shimmerAdapter = ShimmerAdapter(10)

        binding.recyclerCategory.apply {
            layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = shimmerAdapter
        }
        binding.recyclerBestSeller.apply {
            layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = shimmerAdapter
        }
        categoryObserve()
        bestSellerObserve()
        getCategories()
        getBestSeller()
        observeAddress()
        getAddress()
        setPlannerView()

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


    private fun addProductToFavorite(productId: Int, add: Boolean, ) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.AddProductToFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                )
            )
        }
    }
    private fun deleteProductToFavorite(productId: Int, add: Boolean, ) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.DeleteFromFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                )
            )
        }
    }
    private fun setPlannerView() {

        lifecycleScope.launch {
            pannerViewModel.state.collect { state ->
                when (state) {
                    is PannerState.Loading -> {

                    }
                    is PannerState.Success -> {
                        val adapter = BannerAdapter(state.banners)
                        viewPager = binding.slider
                        viewPager.adapter = adapter
                        startAutoSlider(state.banners.size)
                    }
                    is PannerState.Error -> {
                        Log.d("TAG", "setPlannerView:${state.message} ")
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        pannerViewModel.handleIntent(PannerIntent.getPanners)
    }


    private fun startAutoSlider(bannersSize: Int) {

        sliderJob?.cancel()

        sliderJob = lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                delay(1500)

                if (bannersSize > 1) {
                    currentPage += direction


                    if (currentPage == bannersSize - 1) {
                        direction = -1
                    }

                    else if (currentPage == 0) {
                        direction = 1
                    }

                    viewPager.setCurrentItem(currentPage, true)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        sliderJob?.cancel()
    }
}


