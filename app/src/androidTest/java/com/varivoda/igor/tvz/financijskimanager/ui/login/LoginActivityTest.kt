package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var context: Context

    @JvmField
    @Rule
    var mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testLogin() {
        Intents.init()
        onView(withId(R.id.usernameInput)).perform(typeText("admin"),closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText("test"),closeSoftKeyboard())
        onView(withId(R.id.usernameInput)).perform(replaceText("novo ime"),closeSoftKeyboard())
        onView(withId(R.id.rememberMeCheckBox)).perform(click())
        onView(
            Matchers.allOf(
                withId(R.id.loginButton),
                ViewMatchers.withText(context.getString(R.string.login))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.loginButton)).perform(click())
        Intents.intended(hasComponent(HomeActivity::class.java.name))
        Intents.release()
    }

}

