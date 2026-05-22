package otus.demo.totalcoverage.di

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.R

class SimpleFirstTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Test
    fun check_no_expenses_shown() {
        onView(withId(R.id.empty_text)).check(
            matches(
                anyOf(isDisplayed(), withText("Test"))
            )
        )
    }

    @Test
    fun addExpenseFlow() {
        onView(withId(R.id.add_expense_fab)).perform(click())

        onView(withId(R.id.title_edittext)).perform(ViewActions.typeText("test"))
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("100"))
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("test comment"))
        onView(withId(R.id.category_recycler)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click())
        )

        onView(withId(R.id.submit_button)).perform(click())

        retry {
            onView(withId(R.id.expenses_recycler)).check(
                matches(
                    MyCustomMatcher(
                        position = 0,
                        matcher = hasDescendant(withText("test"))
                    )
                )
            )
            onView(withId(R.id.empty_text))
                .check(matches(not(isDisplayed())))
        }
    }
}

fun retry(attempts: Int = 10, block: () -> Unit) {
    repeat(attempts) {
        try {
            block()
            return@repeat
        } catch (e: Throwable) {
            println("Attempt $it failed, retrying in is...")
            Thread.sleep(1000)
        }
    }
}

class MyCustomMatcher(
    private val position: Int,
    private val matcher: Matcher<View>,
) : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun matchesSafely(rv: RecyclerView?): Boolean {
        val view = rv?.findViewHolderForAdapterPosition(position)?.itemView
        return matcher.matches(view)
    }

    override fun describeTo(description: Description?) {
        description?.appendText("not match")
    }
}