package com.varivoda.igor.tvz.financijskimanager.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginActivity
import com.varivoda.igor.tvz.financijskimanager.ui.menu.ViewHolder
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//@RunWith(AndroidJUnit4.class)
class LoginActivityTest {

    /*@Rule @JvmField
    var activityRule = ActivityTestRule<LoginActivity>(
        LoginActivity::class.java
    )*/
    /*@Rule
    @JvmField
    var activityRule = ActivityTestRule<LoginActivity>(
        LoginActivity::class.java
    )*/

    @Test
    fun testLogin(){
        onView(allOf(withId(R.id.loginButton),
            withText("LOGIN")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testLoginButton(){
        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.statistics))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerView(){
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<ViewHolder>(5))
    }
}