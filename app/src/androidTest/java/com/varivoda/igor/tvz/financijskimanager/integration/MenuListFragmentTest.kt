package com.varivoda.igor.tvz.financijskimanager.integration

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.menu.MenuListFragment
import com.varivoda.igor.tvz.financijskimanager.ui.menu.MenuListFragmentDirections
import com.varivoda.igor.tvz.financijskimanager.ui.menu.ViewHolder
import kotlinx.android.synthetic.main.menu_list_fragment.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class MenuListFragmentTest {

    @Test
    fun openBillFragment() = runBlockingTest{
        val navController = mock(NavController::class.java)
        val args = bundleOf("item" to "insert_bill")
        launchFragmentInContainer(fragmentArgs = args) {
            MenuListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
        onView(withId(R.id.menuListRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))

        verify(navController).navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Račun sa najviše stavki po godini"))
    }
}