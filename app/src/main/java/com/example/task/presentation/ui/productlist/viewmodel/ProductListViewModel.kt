package com.example.task.presentation.ui.productlist.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.R
import com.example.task.data.entity.Product
import com.example.task.presentation.ui.scanproduct.repo.ProductsListRepo
import com.example.task.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val context: Context,
    private val repository: ProductsListRepo
) : ViewModel() {


    private val _products: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val products = _products as LiveData<Resource<List<Product>>>


    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts()
                .onStart {
                    withContext(Dispatchers.Main) {
                        _products.value = Resource(
                            Resource.Status.LOADING,
                            null,
                            context.getString(R.string.loading)
                        )
                    }
                }.catch { error ->
                    withContext(Dispatchers.Main) {
                        _products.value = Resource(
                            Resource.Status.ERROR,
                            null,
                            context.getString(R.string.failed_to_load_data)
                        )
                    }
                }.collect { result ->
                    withContext(Dispatchers.Main) {
                        _products.value = Resource(
                            Resource.Status.SUCCESS,
                            result,
                            context.getString(R.string.products_fetched_successfully)
                        )
                    }
                }
        }
    }

}