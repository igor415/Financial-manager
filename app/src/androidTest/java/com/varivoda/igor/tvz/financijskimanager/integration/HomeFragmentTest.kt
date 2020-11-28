package com.varivoda.igor.tvz.financijskimanager.integration

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeFragment
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeFragmentDirections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    @Test
    fun openMenuListFragmentStatistics()  {
        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer {
            HomeFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if(viewLifecycleOwner != null){
                        Navigation.setViewNavController(fragment.requireView(),navController)
                    }
                }
            }
        }

        /*scenario.onFragment {
            Navigation.setViewNavController(it.requireView(),navController)
        }*/

        onView(withId(R.id.statistics)).perform(click())
            //.perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                //hasDescendant(withText("TITLE1")), click()))

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToMenuListFragment("statistics")
        )
    }

    @Test
    fun openMenuListFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInContainer { HomeFragment().also {
            fragment -> fragment.viewLifecycleOwnerLiveData.observeForever {
            if(it != null){
                Navigation.setViewNavController(fragment.requireView(),navController)
            }
        }
        } }

        onView(withText("Products")).perform(click())

        verify(navController).navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment("products"))
    }

    @Test
    fun openMenuListFragmentEmployees() {
        val navController = mock(NavController::class.java)
        launchFragmentInContainer { HomeFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever {
                if(it!=null){
                    Navigation.setViewNavController(fragment.requireView(),navController)
                }
            }
        } }

        onView(withId(R.id.employees)).perform(click())

        verify(navController).navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment("employees"))
    }
}