package com.example.task.presentation.ui.productlist.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.task.R
import com.example.task.data.database.entity.Product
import com.example.task.domain.repo.ProductsListRepo
import com.example.task.presentation.utils.Constants.PRODUCTS_LIST_MINIMUM_COUNT
import com.example.task.presentation.utils.Constants.PRODUCT_EXPIRED_DATE_STATUS_THREAD
import com.example.task.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager,
    private val repository: ProductsListRepo
) : ViewModel() {


    private val _products: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val products = _products as LiveData<Resource<List<Product>>>


    @ObsoleteCoroutinesApi
    fun getProducts() {
        viewModelScope.launch(newSingleThreadContext(PRODUCT_EXPIRED_DATE_STATUS_THREAD)) {
            repository.checkProductsExpiredDateStatus()
            repository.getProducts()
                .onStart {
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
                        if (result.size < PRODUCTS_LIST_MINIMUM_COUNT)
                            _products.value = Resource(
                                Resource.Status.SUCCESS,
                                emptyList(),
                                context.getString(
                                    R.string.num_of_products_is_less_than_allowed_num,
                                    PRODUCTS_LIST_MINIMUM_COUNT
                                )
                            )
                        else
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