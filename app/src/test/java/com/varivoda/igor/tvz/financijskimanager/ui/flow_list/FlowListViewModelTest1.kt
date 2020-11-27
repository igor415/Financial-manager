package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.repository.FakeCustomerRepository
import com.varivoda.igor.tvz.financijskimanager.repository.FakeEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.repository.FakeProductRepository
import com.varivoda.igor.tvz.financijskimanager.repository.FakeStoreRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlowListViewModelTest1 {

    private lateinit var flowListViewModel: FlowListViewModel
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        val storeRepository = FakeStoreRepository()
        val productRepository = FakeProductRepository()
        val employeeRepository = FakeEmployeeRepository()
        val customerRepository = FakeCustomerRepository()
        flowListViewModel = FlowListViewModel(storeRepository,productRepository,employeeRepository,customerRepository)
    }

    @Test
    fun clearProductInfo_isCleared(){
        flowListViewModel.title = "naslov"
        flowListViewModel.clearProductInfo()
        assertThat(flowListViewModel.title, `is`(equalTo("")))
    }

    @Test
    fun onConfirmedClicked_clearedAfter(){
        flowListViewModel.nameInput = "naziv"
        flowListViewModel.priceInput = "12.0"
        flowListViewModel.onConfirmClicked()
        assertEquals("",flowListViewModel.nameInput)
    }
}