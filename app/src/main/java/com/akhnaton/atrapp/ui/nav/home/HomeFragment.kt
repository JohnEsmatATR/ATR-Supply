package com.akhnaton.atrapp.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerState
import com.akhnaton.atrapp.databinding.FragmentHomeBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.home.panner.BannerAdapter
import com.akhnaton.atrapp.ui.nav.home.panner.PannerViewModel
import com.akhnaton.atrapp.ui.nav.home.product.ProductsActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var orderTypeAdapter: OrderTypeAdapter
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


        setupRecycler()
        observeViewModel()
        setPlannerView()
        getAddress()
        observeAddress()


        lifecycleScope.launch {
            categoryViewModel.categoryIntent.send(CategoryIntent.GetCategories)
        }

        binding.cardAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressesActivity::class.java)
            startActivity(intent)
        }
        return binding.root

    }

    private fun setupRecycler() {
        orderTypeAdapter = OrderTypeAdapter { category, orderTypeIndex, _ ->
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            intent.putExtra("flag", orderTypeIndex)
            intent.putExtra("categoryId", category.ID)
            startActivity(intent)
        }


        binding.recyclerPharma.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderTypeAdapter
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

                        state.data.data?.let {
                            orderTypeAdapter.setData(it)
                        }
                    }

                    is CategoryStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        binding.recyclerPharma.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.error ?: "Error", Toast.LENGTH_SHORT).show()
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
                        if (it.result.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "Received: GetProducts")

                            val addresses = it.result.data ?: emptyList()


                            addresses.forEach { address ->
                                if (address.prime == 1) {
                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
                                    binding.defaultAddress.text=address.TITLE
                                }
                            }

                            val hasDefault = addresses.any { address -> address.prime == 1 }
                            if (!hasDefault) {
                                val intent = Intent(requireContext(), AddressesActivity::class.java)
                                startActivity(intent)
                            }

                        } else {
                            hideProgressDialog(binding.progressLoading)
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

                        if (state.data.status == 200){
                            val response = state.data.data

                            response?.let {
                                val adapter = BannerAdapter(it.banners)
                                viewPager = binding.slider
                                viewPager.adapter = adapter
                                startAutoSlider(it.banners.size)

                                Glide.with(requireContext())
                                    .load(it.customer_backgound_image)
                                    .into(binding.imgDeals)
                            }
                        }
                        else {
                            showToastSnack(state.data.message, true)
                        }

                    }


                    is PannerState.Error -> {

                        showToastSnack(state.message, true)
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


