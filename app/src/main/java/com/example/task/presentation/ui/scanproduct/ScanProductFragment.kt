package com.example.task.presentation.ui.scanproduct

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.task.R
import com.example.task.data.database.model.Product
import com.example.task.databinding.FragmentScanProductBinding
import com.example.task.presentation.utils.*
import com.example.task.presentation.utils.Constants.ALLOWED_DIFF_DAYS
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.permissionx.guolindev.PermissionX
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

            if (diffDaysBetweenTwoTimes(
                    customCalendar.timeInMillis,
                    currentTime()
                ) > ALLOWED_DIFF_DAYS
            ) {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.expired_date_warn)
                builder.setMessage(R.string.mocking_message)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing yes action
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                   val mockedDate= customCalendar.timeInMillis.mockDate(requireContext())
                    insertProduct(mockedDate)
                }
                //performing cancel action
                builder.setNeutralButton(getString(R.string.edit_date)) { _, _ ->

                }
                //performing negative action
                builder.setNegativeButton(getString(R.string.no)) { _, _ ->
                    insertProduct(customCalendar.timeInMillis)
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

            } else {
                insertProduct(customCalendar.timeInMillis)
            }
        }

        setupCamera()
        setObservers()
    }

    private fun insertProduct(expiryDate: Long) {
        viewModel.insertProduct(
            Product(
                code = binding.productCodeEt.text.toString(),
                name = binding.productNameEt.text.toString(),
                type = if (::radio.isInitialized) radio.text.toString() else "",
                expiredDate = expiryDate
            )
        )
    }

    private fun setObservers() {

        viewModel.productCode.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (getString(it).isBlank())
                binding.productCodeIl.error = null
            else
                binding.productCodeIl.error = getString(it)
        })
        viewModel.productName.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (getString(it).isBlank())
                binding.productNameIl.error = null
            else
                binding.productNameIl.error = getString(it)
        })
        viewModel.productType.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (getString(it).isNotEmpty())
                showSnack(getString(it))
        })
        viewModel.productCode.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            if (getString(it).isNotEmpty())
                showSnack(getString(it))
        })
        viewModel.productDate.observe(viewLifecycleOwner, {
            showSnack(getString(it))
        })
        viewModel.insertEvent.observe(viewLifecycleOwner, {
            binding.progressCircular.gone()
            showSnack(getString(it))
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
                requestCameraPermission()
            }
        }
    }

    private fun requestCameraPermission() {
        PermissionX.init(activity)
            .permissions( Manifest.permission.CAMERA)
            .request { allGranted, _, _ ->
                if (allGranted) {
                    bindCameraUseCases()
                } else {
                    showSnack(getString(R.string.you_can_enter_barcode_manually))
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
            InputImage.fromMediaImage(imageProxy.image, imageProxy.imageInfo.rotationDegrees)

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

