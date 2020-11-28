package com.varivoda.igor.tvz.financijskimanager.integration

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.FakeProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.service_locator.ServiceLocator
import com.varivoda.igor.tvz.financijskimanager.ui.quarter.QuarterFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class QuarterFragmentTest {

    private lateinit var productRepository: BaseProductRepository

    @Before
    fun initRepository() {
        productRepository = FakeProductRepository()
        ServiceLocator.productRepository = productRepository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun launchFragmentAndGetThirdQuarterProduct() = runBlockingTest {
        launchFragmentInContainer<QuarterFragment>(null, R.style.AppTheme)
        onView(withId(R.id.quarterView)).perform(click())
        onView(withId(R.id.quarterView)).perform(click())
        productRepository.getProductPerQuarter("2020")
        onView(withId(R.id.resultTextView)).check(matches(isDisplayed()))
        onView(withText("ananas")).check(matches(isDisplayed()))
    }


}