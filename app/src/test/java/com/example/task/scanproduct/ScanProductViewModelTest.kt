package com.example.task.scanproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.task.data.FakeScanProductRepoTest
import com.example.task.data.database.entity.Product
import com.example.task.mainCoroutineRule
import com.example.task.presentation.ui.scanproduct.viewmodel.ScanProductViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ScanProductViewModelTest {

    private lateinit var tasksRepository: FakeScanProductRepoTest

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = mainCoroutineRule()
    private lateinit var scanProductViewModelTest: ScanProductViewModel

    @Before
    fun setUp() {
        scanProductViewModelTest = ScanProductViewModel(FakeScanProductRepoTest())
    }


    @Test
    fun `empty product code returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "",
                name = "pin killer",
                type = "Drug",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `blank product code returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = " ",
                name = "pin killer",
                type = "Drug",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `empty product name returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = "",
                type = "Drug",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `blank product name returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = " ",
                type = "Drug",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `empty product type returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = "name",
                type = "",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `blank product type returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = "name",
                type = " ",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `wrong expiry date of product returns false`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = "name",
                type = "",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertFalse(result)
    }

    @Test
    fun `valid product returns true`() {

        val result = scanProductViewModelTest.productDateValidation(
            Product(
                code = "code",
                name = "name",
                type = "Drug",
                expiredDate = System.currentTimeMillis() + 100
            )
        )
        Assert.assertTrue(result)
    }
}