package id.idham.footballmatchschedule.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import id.idham.footballmatchschedule.R.id.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testAppBehaviour() {

        Thread.sleep(3000)

        onView(withId(rv_next_match)).check(matches(isDisplayed()))
        onView(withId(rv_next_match)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        Thread.sleep(2500)

        onView(withId(add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())

        Thread.sleep(1500)

        pressBack()

        onView(withId(teams)).check(matches(isDisplayed()))
        onView(withId(teams)).perform(click())

        Thread.sleep(3000)

        onView(withId(rv_teams)).check(matches(isDisplayed()))
        onView(withId(rv_teams)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(2500)

        onView(withId(add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())

        Thread.sleep(1500)

        pressBack()

        onView(withId(favorites)).check(matches(isDisplayed()))
        onView(withId(favorites)).perform(click())

        Thread.sleep(1500)

        onView(withId(schedule_pager)).check(matches(isDisplayed()))
        onView(withId(schedule_pager)).perform(swipeLeft())

        Thread.sleep(1500)
    }
}
