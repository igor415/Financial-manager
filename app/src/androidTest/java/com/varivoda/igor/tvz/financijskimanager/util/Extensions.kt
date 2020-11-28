package com.varivoda.igor.tvz.financijskimanager.util

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    Handler(Looper.getMainLooper()).post {
        this.observeForever(observer)
    }


    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
    checkNotNull(itemMatcher)
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder =
                view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}