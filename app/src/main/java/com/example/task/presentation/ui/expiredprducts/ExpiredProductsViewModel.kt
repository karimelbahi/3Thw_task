package com.example.task.presentation.ui.expiredprducts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.R
import com.example.task.data.database.model.Product
import com.example.task.domain.repo.ProductsRepo
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
class ExpiredProductsViewModel @Inject constructor(
    private val context: Context,
    private val repository: ProductsRepo
) : ViewModel() {


    private val _expiredProducts: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val expiredProducts = _expiredProducts as LiveData<Resource<List<Product>>>

    fun getExpiredProducts() {
        viewModelScope.launch {
            repository.getExpiredProducts()
                .onStart {
                    withContext(Dispatchers.Main) {
                        _expiredProducts.value = Resource(
                            Resource.Status.LOADING,
                            null,
                            context.getString(R.string.loading)
                        )
                    }
                }.catch { error ->
                    withContext(Dispatchers.Main) {
                        _expiredProducts.value = Resource(
                            Resource.Status.ERROR,
                            null,
                            context.getString(R.string.failed_to_load_data)
                        )
                    }
                }.collect { result ->
                    withContext(Dispatchers.Main) {
                        _expiredProducts.value = Resource(
                            Resource.Status.SUCCESS,
                            result,
                            context.getString(R.string.products_fetched_successfully)
                        )
                    }
                }
        }
    }

}