package com.akhnaton.atrapp.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.Category
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerState
import com.akhnaton.atrapp.databinding.FragmentHomeBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.HorizontalSpacingItemDecoration
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.home.categories.CategoryAdapter
import com.akhnaton.atrapp.ui.nav.home.panner.BannerAdapter
import com.akhnaton.atrapp.ui.nav.home.panner.PannerViewModel
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()

    //    private lateinit var orderTypeAdapter: OrderTypeAdapter
    private lateinit var orderTypeAdapter2: OrderTypeAdapter2
    private val viewModel: AddressesViewModel by viewModels()

    private val pannerViewModel: PannerViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private var currentPage = 0
    private var direction = 1
    private var sliderHandler: Handler? = null
    private var sliderRunnable: Runnable? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var bestSellersAdapter: BestSellersAdapter
    private lateinit var newArrivalsAdapter: NewArrivalsAdapter

    lateinit var adapter: ProductAdapter

    private var bestSellers: MutableList<ProductModel> = ArrayList()

    var orderTypeIndex = ""
    lateinit var category: CategoriesModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)


        guestHandling()
        setupRecycler2()
        observeViewModel()
        setPlannerView()
        getAddress()
        observeAddress()
//        setupCategories()
//        setupBestsellers()
        getBestSeller()
        setupNewArrivals()
        bestSellerObserve()

        lifecycleScope.launch {
            categoryViewModel.categoryIntent.send(CategoryIntent.GetCategories)
        }

        binding.cardAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressesActivity::class.java)
            startActivity(intent)
        }

        binding.layoutSearch.setOnClickListener {
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            intent.putExtra("flag", orderTypeIndex)
            intent.putExtra("categoryId", category.ID)
            startActivity(intent)
        }

        return binding.root
    }

    private fun guestHandling() {
        if (SharedPreferenceHelper.isLogged == false) {
            binding.cardAddress.visibility = View.GONE
            binding.tvWelcome.text = "${getString(R.string.welcome_guest)}"
        } else {
            binding.tvWelcome.text =
                "${getString(R.string.welcome)} ${SharedPreferenceHelper.userObj?.first_name}"
        }
    }

//    private fun setupRecycler() {
//
//        orderTypeAdapter = OrderTypeAdapter { category, orderTypeIndex, _ ->
//            val intent = Intent(requireContext(), ProductsActivity::class.java)
//            intent.putExtra("flag", orderTypeIndex)
//            intent.putExtra("categoryId", category.ID)
//            startActivity(intent)
//        }
//
//
//        binding.recyclerPharma.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = orderTypeAdapter
//            setHasFixedSize(true)
//        }
//    }

    private fun setupRecycler2() {
        orderTypeAdapter2 = OrderTypeAdapter2 { category, orderTypeIndex, _ ->
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            intent.putExtra("flag", orderTypeIndex)
            intent.putExtra("categoryId", category.ID)
            startActivity(intent)
        }


        binding.recyclerPharma.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = orderTypeAdapter2
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            categoryViewModel.state.collect { state ->
                when (state) {
                    is CategoryStatus.Idle -> Unit
                    is CategoryStatus.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }

                    is CategoryStatus.GetCategory -> {
                        hideProgressDialog(binding.progressLoading)
                        binding.recyclerPharma.visibility = View.VISIBLE

                        orderTypeIndex = state.data.data!![0].order_type_index
                        category = state.data.data[0].categories[0]
                        state.data.data?.let {
                            orderTypeAdapter2.setData(it)
                        }
                    }

                    is CategoryStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        binding.recyclerPharma.visibility = View.VISIBLE
                        //   Toast.makeText(requireContext(), state.error ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeAddress() {
        lifecycleScope.launch {
            viewModel.state.collect {
                //  Log.d("DEBUGGGGG", "Received state: $it")
                when (it) {
                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idleeeee")
                    is AddressStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        // showProgressDialog(binding.progressLoading)
                    }

                    is AddressStatus.GetMyAddresses -> {
                        if (it.result.status == 200) {
                            //   hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "Received: GetProducts")

                            val addresses = it.result.data ?: emptyList()


                            addresses.forEach { address ->
                                if (address.prime == 1) {
                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
                                    binding.defaultAddress.text = address.TITLE
                                }
                            }

                            val hasDefault = addresses.any { address -> address.prime == 1 }
                            if (!hasDefault) {
                                val intent = Intent(requireContext(), AddressesActivity::class.java)
                                startActivity(intent)
                            }

                        } else {
                            //   hideProgressDialog(binding.progressLoading)
                        }
                    }


                    is AddressStatus.MakeAddressPrime -> {}

                    is AddressStatus.Error -> {

                    }

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

    private fun setPlannerView() {
        lifecycleScope.launch {
            pannerViewModel.state.collect { state ->
                when (state) {
                    is PannerState.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }

                    is PannerState.Success -> {
                        hideProgressDialog(binding.progressLoading)

                        if (state.data.status == 200) {
                            val response = state.data.data

                            if (response != null && !response.banners.isNullOrEmpty()) {
                                val banners = response.banners

                                binding.slider.visibility = View.VISIBLE
                                binding.tabLayout.visibility = View.VISIBLE
                                binding.imgDefaultLogo.visibility = View.GONE

                                val adapter = BannerAdapter(banners)
                                viewPager = binding.slider
                                viewPager.adapter = adapter
                                binding.tabLayout.setupWithViewPager(viewPager)
                                startAutoSlider(banners.size)
                            } else {
                                binding.slider.visibility = View.GONE
                                binding.tabLayout.visibility = View.GONE
                                binding.imgDefaultLogo.visibility = View.VISIBLE
                            }

                            response?.customer_backgound_image?.let { bgImg ->
                                Glide.with(requireContext())
                                    .load(bgImg)
                                    .into(binding.imgDeals)
                            }
                        } else {
                            binding.slider.visibility = View.GONE
                            binding.tabLayout.visibility = View.GONE
                            binding.imgDefaultLogo.visibility = View.VISIBLE
                        }
                    }

                    is PannerState.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        binding.slider.visibility = View.GONE
                        binding.tabLayout.visibility = View.GONE
                        binding.imgDefaultLogo.visibility = View.VISIBLE
                    }
                }
            }
        }

        pannerViewModel.handleIntent(PannerIntent.getPanners)
    }


    private fun startAutoSlider(bannersSize: Int) {
        if (bannersSize <= 1) return

        sliderHandler = Handler(Looper.getMainLooper())

        sliderRunnable = object : Runnable {
            override fun run() {
                currentPage += direction

                if (currentPage == bannersSize - 1) {
                    direction = -1
                } else if (currentPage == 0) {
                    direction = 1
                }

                viewPager.setCurrentItem(currentPage, true)

                sliderHandler?.postDelayed(this, 3000)
            }
        }

        sliderHandler?.postDelayed(sliderRunnable!!, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler?.removeCallbacks(sliderRunnable!!)
        sliderHandler = null
        sliderRunnable = null
    }

    private fun setupCategories() {
        val categories = listOf(
            Category(
                name = "Pharma",
                imageRes = R.drawable.pharma
            ),
            Category(
                name = "Cosmetics",
                imageRes = R.drawable.cosmetics
            ),
            Category(
                name = "Mounjaro",
                imageRes = R.drawable.mounjaro
            ),
            Category(
                name = "Supplements",
                imageRes = R.drawable.supplements
            )
        )
        categoryAdapter = CategoryAdapter(
            categories
        ) { category ->
            Log.d(
                "CATEGORY",
                "Selected: ${category.name}"
            )
            // Handle category click here
        }
        binding.recyclerCategories.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupBestsellers(bestSellers1: MutableList<ProductModel>) {
        bestSellersAdapter = BestSellersAdapter(
            onClick = { product, position, sharedView, transitionName ->
            },
            onFavoriteClick = { product, position, isFavorite ->
            },
            onAddToCartClick = { product ->
            }
        )
        bestSellersAdapter.setData(bestSellers1, false, "Pharma")

        binding.recyclerBestSellers.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestSellersAdapter
            addItemDecoration(
                HorizontalSpacingItemDecoration(
                    resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._4sdp)
                )
            )
        }
    }

    private fun setupNewArrivals() {
        val newArrivals = listOf(
            ProductModel(
                ID = 3949400,
                TITLE = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                DESCRIPTION = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                IMAGE_URL = "https://sales.atr-eg.com/customer/uploads/products/40050.jpeg",
                QUANTITY = 8500,
                MY_QUANTITY = 0,
                WEIGHT = "100 GM",
                QOUTA = 0,
                TAX = 0.0,
                ITEM_TYPE = "Pharma",
                IS_BEST_SELLER = false,
                IS_LIKED = false,
                PRICE_WITHOUT_TAX = 8019.8,
                PRICE_DISCOUNT = 0.0,
                PRICE_DISCOUNT_PERCENTAGE = "0%",
                PRICE_AFTER_DISCOUNT = 8019.8,
                PRICE_WITH_TAX = 8019.8,
                BONUS_DATA = emptyList(),
                MY_QUANTITY_TOTAL_PRICE = 0.0,
                RATE = "5.0",
                IN_STOCK = true,
                HAS_BONUS = true,
            ),
            ProductModel(
                ID = 3949400,
                TITLE = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                DESCRIPTION = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                IMAGE_URL = "https://sales.atr-eg.com/customer/uploads/products/40050.jpeg",
                QUANTITY = 8500,
                MY_QUANTITY = 0,
                WEIGHT = "100 GM",
                QOUTA = 0,
                TAX = 0.0,
                ITEM_TYPE = "Pharma",
                IS_BEST_SELLER = false,
                IS_LIKED = false,
                PRICE_WITHOUT_TAX = 8019.8,
                PRICE_DISCOUNT = 0.0,
                PRICE_DISCOUNT_PERCENTAGE = "0%",
                PRICE_AFTER_DISCOUNT = 8019.8,
                PRICE_WITH_TAX = 8019.8,
                BONUS_DATA = emptyList(),
                MY_QUANTITY_TOTAL_PRICE = 0.0,
                RATE = "5.0",
                IN_STOCK = true,
                HAS_BONUS = true,
            ),
            ProductModel(
                ID = 3949400,
                TITLE = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                DESCRIPTION = "MOUNJARO KWIKPEN 2.5MG/0.6ML 3ML X1",
                IMAGE_URL = "https://sales.atr-eg.com/customer/uploads/products/40050.jpeg",
                QUANTITY = 8500,
                MY_QUANTITY = 0,
                WEIGHT = "100 GM",
                QOUTA = 0,
                TAX = 0.0,
                ITEM_TYPE = "Pharma",
                IS_BEST_SELLER = false,
                IS_LIKED = false,
                PRICE_WITHOUT_TAX = 8019.8,
                PRICE_DISCOUNT = 0.0,
                PRICE_DISCOUNT_PERCENTAGE = "0%",
                PRICE_AFTER_DISCOUNT = 8019.8,
                PRICE_WITH_TAX = 8019.8,
                BONUS_DATA = emptyList(),
                MY_QUANTITY_TOTAL_PRICE = 0.0,
                RATE = "5.0",
                IN_STOCK = true,
                HAS_BONUS = true,
            ),
        )
        newArrivalsAdapter = NewArrivalsAdapter(
            onClick = { product, position, sharedView, transitionName ->
            },
            onFavoriteClick = { product, position, isFavorite ->
            },
            onAddToCartClick = { product ->
            }
        )
        newArrivalsAdapter.setData(newArrivals, false, "Pharma")

        binding.recyclerNewArrivals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = newArrivalsAdapter
            addItemDecoration(
                HorizontalSpacingItemDecoration(
                    resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._4sdp)
                )
            )
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
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            if (it.data.data!!.isNotEmpty()) {
                                bestSellers.addAll(it.data.data!!)
                                setupBestsellers(bestSellers)
                            }
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

    private fun getBestSeller() {
        lifecycleScope.launch {
            bestSellerViewModel.homeIntent.send(
                BestSellerIntent.GetBestSeller(1, "Pharma")
            )
        }
    }
}