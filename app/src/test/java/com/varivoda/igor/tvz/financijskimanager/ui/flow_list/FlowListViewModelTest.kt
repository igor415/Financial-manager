package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*@RunWith(AndroidJUnit4::class)
class FlowListViewModelTest {

    private lateinit var flowListViewModel: FlowListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        flowListViewModel = FlowListViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun clearProductInfo_isCleared(){
        flowListViewModel.title = "naslov"
        flowListViewModel.clearProductInfo()
        assertThat(flowListViewModel.title, `is`(equalTo("")))
    }
}*/