package com.varivoda.igor.tvz.financijskimanager.ui.settings

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.varivoda.igor.tvz.financijskimanager.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

    private lateinit var context: Context

    @JvmField
    @Rule var activityRule = ActivityTestRule(SettingsActivity::class.java,false,true)

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun activityLaunchesSuccessfully() {
        ActivityScenario.launch(SettingsActivity::class.java)
    }

    @Test
    fun testAllSettingsOptions(){
        onView(withText("Choose toast message design")).check(matches(isDisplayed()))
        onView(withText("Vibrations")).check(matches(isDisplayed()))
        onView(withText("Enable or disable notifications")).check(matches(isDisplayed()))
        onView(withText("Change brightness")).check(matches(isDisplayed()))
    }

    @Test
    fun testActionBar(){
        onView(
            withText(R.string.settings)).check(matches(isDisplayed()))
    }
}