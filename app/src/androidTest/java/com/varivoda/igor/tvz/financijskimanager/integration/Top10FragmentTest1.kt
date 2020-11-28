package com.varivoda.igor.tvz.financijskimanager.integration

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.FakeProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.service_locator.ServiceLocator
import com.varivoda.igor.tvz.financijskimanager.ui.top10.Top10Fragment
import com.varivoda.igor.tvz.financijskimanager.util.atPosition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class Top10FragmentTest{

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
    fun getTop10Products() = runBlockingTest {
        launchFragmentInContainer<Top10Fragment>(bundleOf("text" to "naslov"), R.style.AppTheme)
        onView(withId(R.id.timePeriod)).check(matches(isDisplayed()))
        productRepository.getTop10Products("","")
        onView(withId(R.id.topRecyclerView))
            .check(matches(atPosition(0, hasDescendant(withText("name")))))
    }


}