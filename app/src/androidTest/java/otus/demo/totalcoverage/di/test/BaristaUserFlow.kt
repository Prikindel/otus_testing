package otus.demo.totalcoverage.di.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo
import com.adevinta.android.barista.rule.flaky.AllowFlaky
import com.adevinta.android.barista.rule.flaky.FlakyTestRule
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.di.atRecyclerPosition
import otus.demo.totalcoverage.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition

@RunWith(AndroidJUnit4::class)
class BaristaUserFlow {

    private val flakyRule = FlakyTestRule()

    private val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @get:Rule
    var chain: RuleChain = RuleChain.outerRule(flakyRule).around(activityRule)

    @Test
    @AllowFlaky(attempts = 5)
    fun addExpenseFlow() {
        assertDisplayed("No expenses :)")

        clickOn(R.id.add_expense_fab)

        typeTo(R.id.title_edittext, "Test")
        typeTo(R.id.amount_edittext, "10")
        typeTo(R.id.comment_edittext, "test comment")

        clickOn(R.id.submit_button)

        clickOn(R.id.add_expense_fab)

        typeTo(R.id.title_edittext, "Test")
        typeTo(R.id.amount_edittext, "10")
        typeTo(R.id.comment_edittext, "test comment")

        clickOn(R.id.submit_button)

        onView(withId(R.id.expenses_recycler)).check(
            matches(
                atRecyclerPosition(
                    0,
                    allOf(
                        hasDescendant(withId(R.id.tv_name)),
                        hasDescendant(withText("Test")),
                    ),
                ),
            ),
        )

        assertCustomAssertionAtPosition(
            listId = R.id.expenses_recycler,
            position = 0,
            viewAssertion = matches(hasDescendant(withText("Test")))
        )

        retryUntilTwoItems(timeoutMs = 10_000L, stepMs = 1000L) {
            assertRecyclerViewItemCount(R.id.expenses_recycler, 2)
        }
    }
}

private inline fun retryUntilTwoItems(timeoutMs: Long, stepMs: Long, block: () -> Unit) {
    val deadline = System.currentTimeMillis() + timeoutMs
    var last: Throwable? = null
    while (System.currentTimeMillis() < deadline) {
        try {
            block()
            return
        } catch (e: Throwable) {
            last = e
            Thread.sleep(stepMs)
        }
    }
    throw last ?: AssertionError("Ожидание двух элементов списка не удалось за $timeoutMs ms")
}
