package com.example.task.presentation.ui.productlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.databinding.FragmentProductListBinding
import com.example.task.presentation.utils.*
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    val viewModel: ProductListViewModel by viewModels()

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productListAdapter: ProductListAdapter

    @ObsoleteCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductListBinding.bind(view)

        setUpViews()
        setObservers()

    }

    private fun setUpViews() {
        productListAdapter = ProductListAdapter()

        binding.apply {
            progressCircular.visibility = View.VISIBLE
            recyclerViewCountries.apply {
                adapter = productListAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                itemAnimator = null
            }
        }

    }

    @ObsoleteCoroutinesApi
    private fun setObservers() {

        viewModel.getProducts()
        binding.progressCircular.visible()
        viewModel.products.observe(viewLifecycleOwner, { products ->
            when (products.status) {
                Resource.Status.LOADING -> {
                    binding.progressCircular.visible()
                }
                Resource.Status.ERROR -> {
                    binding.progressCircular.invisible()
                    products.message?.let {
                        showSnackBar(it)
                    }
                }
                Resource.Status.SUCCESS -> {
                    binding.progressCircular.invisible()
                    if (products.data.isNullOrEmpty()) {
                        binding.emptyBoxImg.visible()
                        products.message?.let { showSnackBar(it) }
                    }
                    else {
                        binding.emptyBoxImg.gone()
                        productListAdapter.submitList(products.data)
                    }
                }
            }
        })

    }

    private fun showSnackBar(message: String) {
        binding.main.snack(message) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

