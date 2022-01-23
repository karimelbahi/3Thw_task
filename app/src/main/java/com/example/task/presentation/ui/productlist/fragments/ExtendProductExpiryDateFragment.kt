package com.example.task.presentation.ui.productlist.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.task.R
import com.example.task.databinding.FragmentExtendExpiryDateProductBinding
import com.example.task.presentation.ui.productlist.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtendProductExpiryDateFragment : Fragment(R.layout.fragment_extend_expiry_date_product) {

    val viewModel: ProductListViewModel by viewModels()

    private var _binding: FragmentExtendExpiryDateProductBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExtendExpiryDateProductBinding.bind(view)

        setUpViews()
        setObservers()

    }

    private fun setUpViews() {

    }

    private fun setObservers() {

    }
}