package com.varivoda.igor.tvz.financijskimanager.ui.splash

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginActivity
import com.varivoda.igor.tvz.financijskimanager.ui.splashscreen.SplashActivity
import kotlinx.android.synthetic.main.splash_activity.view.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashActivityTest{

    private lateinit var context: Context

    @JvmField
    @Rule
    var mActivityRule: ActivityTestRule<SplashActivity> = ActivityTestRule(SplashActivity::class.java)

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun isLogoDisplayed(){
        onView(withId(R.id.mainLogo)).check(matches(isDisplayed()))
    }
}