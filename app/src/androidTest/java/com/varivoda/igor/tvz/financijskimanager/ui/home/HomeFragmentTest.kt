package com.varivoda.igor.tvz.financijskimanager.ui.home

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ToastMatcher
import com.varivoda.igor.tvz.financijskimanager.ui.menu.ViewHolder
import com.varivoda.igor.tvz.financijskimanager.ui.settings.SettingsActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var context: Context

    @JvmField
    @Rule var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun checkHomeFragmentOptions(){
        onView(withId(R.id.statistics)).check(matches(isDisplayed()))
        onView(withId(R.id.insertInvoice)).check(matches(isDisplayed()))
        onView(withId(R.id.customers)).check(matches(isDisplayed()))
        onView(withId(R.id.stores)).check(matches(isDisplayed()))
        onView(withId(R.id.products)).check(matches(isDisplayed()))
        onView(withId(R.id.employees)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerView(){
        onView(withId(R.id.employees)).perform(click())
        onView(withId(R.id.menuListRecyclerView))
            .perform(
                RecyclerViewActions
                .actionOnItemAtPosition<ViewHolder>(0,click()))
        onView(withText("Popis zaposlenika")).inRoot(ToastMatcher()).check(matches(
            isDisplayed()))
    }

    @Test
    fun testMenu(){
        onView(withId(R.id.more)).perform(click()).inRoot(isPlatformPopup())
        onView(withText(R.string.share)).check(matches(isDisplayed()))
        onView(withText(R.string.enable_all_options)).check(matches(isDisplayed()))
        onView(withText(R.string.report_a_bug)).check(matches(isDisplayed()))
        onView(withText("Other menu")).check(doesNotExist())
    }

    @Test
    fun testDisableHomeItem(){
        onView(withId(R.id.customers)).perform(longClick())
            .inRoot(isPlatformPopup())
        onView(withText("Disable")).perform(click())
        onView(withId(R.id.customers)).check(matches(not(isEnabled())))
        openMenuAndEnableAll()
        onView(withId(R.id.customers)).check(matches(isEnabled()))
    }

    private fun openMenuAndEnableAll() {
        onView(withId(R.id.more)).perform(click()).inRoot(isPlatformPopup())
        onView(withText(R.string.enable_all_options)).perform(click())
    }

    @Test
    fun testSettingsActivity(){
        Intents.init()
        onView(withId(R.id.settings)).perform(click())
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.release()
    }


}