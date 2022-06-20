package br.com.android.gitrepos

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.android.gitrepos.adapter.GitRepoAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Carlos Souza on 18,junho,2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testActivity_inView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun testVisibility_recycler_view() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.rv_repo_info))
            .check(matches(isDisplayed()))

    }

    @Test
    fun test_recycler_view_move() {
        onView(withId(R.id.rv_repo_info))
             .perform(scrollToPosition<GitRepoAdapter.ReposViewHolder>(10))
        Thread.sleep(5000)
        onView(withId(R.id.rv_repo_info))
            .perform(scrollToPosition<GitRepoAdapter.ReposViewHolder>(20))
    }
}