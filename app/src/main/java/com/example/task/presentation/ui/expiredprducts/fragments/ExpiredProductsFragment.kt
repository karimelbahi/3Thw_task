package com.example.task.presentation.ui.expiredprducts.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.task.R
import com.example.task.databinding.FragmentExpiredProductsBinding
import com.example.task.presentation.ui.productlist.adapter.ExpiredProductsAdapter
import com.example.task.presentation.ui.productlist.viewmodel.ExpiredProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpiredProductsFragment : Fragment(R.layout.fragment_expired_products) {

    private val viewModel: ExpiredProductsViewModel by viewModels()

    private var _binding: FragmentExpiredProductsBinding? = null
    private val binding get() = _binding!!

    lateinit var expiredProductsAdapter: ExpiredProductsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExpiredProductsBinding.bind(view)


    }

    private fun setObservers() {
        viewModel.getExpiredProducts()



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

