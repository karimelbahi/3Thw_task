package com.example.task.presentation.ui.scanproduct.viewmodel

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.R
import com.example.task.app.MyApplication
import com.example.task.data.database.entity.Product
import com.example.task.domain.repo.ScanProductRepo
import com.example.task.presentation.utils.convertLongToStrDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class ScanProductViewModel @Inject constructor(
    private val repository: ScanProductRepo
) : ViewModel() {

     val productCode: MutableLiveData<Int> = MutableLiveData()
     val productName: MutableLiveData<Int> = MutableLiveData()
     val productType: MutableLiveData<Int> = MutableLiveData()
     val productDate: MutableLiveData<Int> = MutableLiveData()

    private val _insertEvent: MutableLiveData<Int> = MutableLiveData()
    val insertEvent = _insertEvent as LiveData<Int>

    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()
                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(MyApplication.applicationContext())
                cameraProviderFuture.addListener(
                    Runnable {
                        try {
                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
                        } catch (e: ExecutionException) {
                            Log.e(TAG, "Unhandled exception", e)
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Unhandled exception", e)
                        }
                    },
                    ContextCompat.getMainExecutor(MyApplication.applicationContext())
                )
            }
            return cameraProviderLiveData!!
        }

    fun insertProduct(product: Product) {

        if (productDateValidation(product))
            viewModelScope.launch {
                val response = repository.insertProduct(product)
                if (response > 0) {
                    withContext(Dispatchers.Main) {
                        _insertEvent.postValue(R.string.product_added_successful)
                    }
                }
            }
    }

     fun productDateValidation(product: Product): Boolean {
        var validate = true

        if (product.code.isBlank()) {
            validate = false
            productCode.value = R.string.enter_valid_product_code
        } else
            productCode.value = R.string.empty

        if (product.name.isBlank()) {
            validate = false
            productName.value = R.string.enter_valid_product_name
        } else
            productName.value = R.string.empty

        when {
            product.type.isBlank() -> {
                validate = false
                productType.value = R.string.select_product_type
            }

            product.expiredDate.convertLongToStrDate() < System.currentTimeMillis().convertLongToStrDate() -> {
                validate = false
                productDate.value = R.string.select_correct_expire_date
            }
        }
        return validate
    }


    companion object {
        private const val TAG = "ScanProductViewModel"
    }

}