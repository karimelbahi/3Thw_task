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
import com.example.task.data.entity.Product
import com.example.task.presentation.ui.scanproduct.repo.ScanProductRepo
import com.example.task.presentation.utils.convertLongToTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class ScanProductViewModel @Inject constructor(
    private val context: Context,
    private val repository: ScanProductRepo
) : ViewModel() {


     val productCode: MutableLiveData<String> = MutableLiveData()
     val productName: MutableLiveData<String> = MutableLiveData()
     val productType: MutableLiveData<String> = MutableLiveData()
     val productDate: MutableLiveData<String> = MutableLiveData()

    private val _insertEvent: MutableLiveData<String> = MutableLiveData()
    val insertEvent = _insertEvent as LiveData<String>

    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()
                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(context)
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
                    ContextCompat.getMainExecutor(context)
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
                        _insertEvent.postValue(context.getString(R.string.product_added_successful))
                    }
                }
            }
    }

    private fun productDateValidation(product: Product): Boolean {
        var validate = true

        if (product.code.isEmpty()) {
            validate = false
            productCode.value = context.getString(R.string.enter_valid_product_code)
        } else
            productCode.value = ""

        if (product.name.isEmpty()) {
            validate = false
            productName.value = context.getString(R.string.enter_valid_product_name)
        } else
            productName.value = ""

        when {
            product.type.isEmpty() -> {
                validate = false
                productType.value = context.getString(R.string.select_product_type)
            }

            product.expiredDate.convertLongToTime() < System.currentTimeMillis().convertLongToTime() -> {
                validate = false
                productDate.value = context.getString(R.string.select_correct_expire_date)
            }
        }
        return validate
    }


    companion object {
        private const val TAG = "CameraXViewModel"
    }

}