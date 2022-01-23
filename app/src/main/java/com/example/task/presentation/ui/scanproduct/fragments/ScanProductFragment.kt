package com.example.task.presentation.ui.scanproduct.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.task.R
import com.example.task.data.entity.Product
import com.example.task.databinding.FragmentScanProductBinding
import com.example.task.presentation.ui.scanproduct.viewmodel.ScanProductViewModel
import com.example.task.presentation.utils.gone
import com.example.task.presentation.utils.snack
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScanProductFragment : Fragment(R.layout.fragment_scan_product) {

    val viewModel: ScanProductViewModel by viewModels()

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null

    private lateinit var radio: RadioButton

    private var _binding: FragmentScanProductBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanProductBinding.bind(view)

        binding.typeRg.setOnCheckedChangeListener { _, checkedId ->
            if (binding.typeRg.checkedRadioButtonId != -1) {
                radio = requireActivity().findViewById(checkedId)
            }
        }

        binding.addProductBtn.setOnClickListener {
            val customCalendar = Calendar.getInstance()
            customCalendar.set(
                binding.dateP.year,
                binding.dateP.month,
                binding.dateP.dayOfMonth,
                binding.timeP.hour,
                binding.timeP.minute
            )

            viewModel.insertProduct(
                Product(
                    code = binding.productCodeEt.text.toString(),
                    name = binding.productNameEt.text.toString(),
                    type = if (::radio.isInitialized) radio.text.toString() else "",
                    expiredDate = customCalendar.timeInMillis
                )
            )
        }
        setupCamera()
        setObservers()
    }


    private fun setObservers() {

        viewModel.productCode.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (it.isBlank())
                binding.productCodeIl.error = null
            else
                binding.productCodeIl.error = it
        })
        viewModel.productName.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (it.isBlank())
                binding.productNameIl.error = null
            else
                binding.productNameIl.error = it
        })
        viewModel.productType.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (it.isNotEmpty())
                showSnack(it)
        })
        viewModel.productCode.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (it.isNotEmpty())
                showSnack(it)
        })
        viewModel.productDate.observe(viewLifecycleOwner, {
            showSnack(it)
        })
        viewModel.insertEvent.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            showSnack(it)
        })
    }

    private fun showSnack(message: String) {
        binding.main.snack(message) {}
    }

    private fun setupCamera() {
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        viewModel.processCameraProvider.observe(viewLifecycleOwner) { provider: ProcessCameraProvider? ->
            cameraProvider = provider
            if (isCameraPermissionGranted()) {
                bindCameraUseCases()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_CAMERA_REQUEST
                )
            }
        }
    }

    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        previewUseCase = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider)

        try {

            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                previewUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindAnalyseUseCase() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()

        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)

        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        // Initialize our background executor
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(
            cameraExecutor,
            ImageAnalysis.Analyzer { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy)
            }
        )

        try {
            cameraProvider!!.bindToLifecycle(
                /* lifecycleOwner= */this,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    binding.productCodeEt.setText(barcode.rawValue)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, it.message ?: it.toString())
            }
            .addOnCompleteListener {
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                imageProxy.close()

            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases()
            } else {
                Log.e(TAG, "no camera permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {

        private val TAG = ScanProductFragment::class.java.simpleName

        private const val PERMISSION_CAMERA_REQUEST = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}

