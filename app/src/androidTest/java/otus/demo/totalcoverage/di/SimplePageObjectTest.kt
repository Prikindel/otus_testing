package otus.demo.totalcoverage.di

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.adevinta.android.barista.internal.matcher.RecyclerViewItemCountAssertion
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.R

class SimplePageObjectTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Test
    fun addExpenseFlow() {
        AllExpensesPage().fabClick()

        AddExpensesPage()
            .fillTitle("test")
            .fillAmount("100")
            .fillComment("test comment")
            .selectCategory(2)
            .submit()

        retry {
            AllExpensesPage()
                .checkExpenses()
                .checkNotEmptyText()
        }
    }

    class AllExpensesPage {
        private val fab = onView(withId(R.id.add_expense_fab))
        private val expensesRecycler = onView(withId(R.id.expenses_recycler))
        private val emptyText = onView(withId(R.id.empty_text))

        fun fabClick() = apply {
            fab.perform(click())
        }

        fun checkExpenses() = apply {
            expensesRecycler.check(
                matches(
                    MyCustomMatcher(
                        position = 0,
                        matcher = hasDescendant(withText("test"))
                    )
                )
            )
            expensesRecycler.check(RecyclerViewItemCountAssertion(1))
        }

        fun checkEmptyText() = apply {
            emptyText.check(matches(isDisplayed()))
        }

        fun checkNotEmptyText() = apply {
            emptyText.check(matches(not(isDisplayed())))
        }
    }

    class AddExpensesPage {
        private val titleEditText = onView(withId(R.id.title_edittext))
        private val amountEditText = onView(withId(R.id.amount_edittext))
        private val commentEditText = onView(withId(R.id.comment_edittext))
        private val categoryRecycler = onView(withId(R.id.category_recycler))
        private val submitButton = onView(withId(R.id.submit_button))

        fun fillTitle(value: String) = apply {
            titleEditText.perform(ViewActions.typeText(value))
        }

        fun fillAmount(value: String) = apply {
            amountEditText.perform(ViewActions.typeText(value))
        }

        fun fillComment(value: String) = apply {
            commentEditText.perform(ViewActions.typeText(value))
        }

        fun selectCategory(value: Int) = apply {
            categoryRecycler.perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(value, click())
            )
        }

        fun submit() = apply {
            submitButton.perform(click())
        }
    }
}