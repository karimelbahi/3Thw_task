package com.example.task.presentation.ui.expiredprducts.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.databinding.FragmentExpiredProductsBinding
import com.example.task.presentation.ui.productlist.adapter.ExpiredProductsAdapter
import com.example.task.presentation.ui.productlist.adapter.ProductListAdapter
import com.example.task.presentation.ui.productlist.viewmodel.ExpiredProductsViewModel
import com.example.task.presentation.utils.Resource
import com.example.task.presentation.utils.invisible
import com.example.task.presentation.utils.snack
import com.example.task.presentation.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpiredProductsFragment : Fragment(R.layout.fragment_expired_products) {

    private val viewModel: ExpiredProductsViewModel by viewModels()

    private var _binding: FragmentExpiredProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var expiredProductsAdapter: ExpiredProductsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExpiredProductsBinding.bind(view)

        setUpViews()
        setObservers()
    }

    private fun setUpViews() {
        expiredProductsAdapter = ExpiredProductsAdapter()

        binding.apply {
            progressCircular.visibility = View.VISIBLE
            recyclerViewCountries.apply {
                adapter = expiredProductsAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                itemAnimator = null
            }
        }

    }

    private fun setObservers() {
        viewModel.getExpiredProducts()
        viewModel.expiredProducts.observe(viewLifecycleOwner, { products ->
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
                    expiredProductsAdapter.submitList(products.data)

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

