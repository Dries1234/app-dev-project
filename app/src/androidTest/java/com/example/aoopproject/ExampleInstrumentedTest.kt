package com.example.aoopproject

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation



import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testAppBar_displaysMenuOptions() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.settings)).check(matches(isDisplayed()))
    }
    @Test
    fun testRecyclerViewDisplayed() {
        val activityScenario = ActivityScenario.launch(FavouritesActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }
}