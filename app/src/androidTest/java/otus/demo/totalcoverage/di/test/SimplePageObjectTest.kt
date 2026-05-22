package otus.demo.totalcoverage.di.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.R
import otus.demo.totalcoverage.di.atRecyclerPosition
import otus.demo.totalcoverage.di.size

class SimplePageObjectTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Test
    fun addExpenseFlow() {
        AllExpensesPage().clickOnFab()

        AddExpensePage()
            .fillTitle("test")
            .fillAmount("100")
            .fillComment("test comment")
            .pressSubmit()

        repeat(10) { attempt ->
            try {
                AllExpensesPage().checkExpenses()
                return
            } catch (_: Throwable) {
                println("Attempt $attempt failed, retrying in 1s...")
                Thread.sleep(1000)
            }
        }
        throw AssertionError("Элемент списка с заголовком 'test' не найден после 10 попыток")
    }
}

class AllExpensesPage {
    private val fabButton = onView(withId(R.id.add_expense_fab))
    private val list = onView(withId(R.id.expenses_recycler))

    fun clickOnFab() {
        fabButton.perform(click())
    }

    fun checkExpenses() {
        list.check(matches(atRecyclerPosition(0, hasDescendant(withText("test")))))
        list.check(matches(size(1)))
    }
}

class AddExpensePage {
    private val titleEditText = onView(withId(R.id.title_edittext))
    private val amountEditText = onView(withId(R.id.amount_edittext))
    private val commentEditText = onView(withId(R.id.comment_edittext))
    private val submitButton = onView(withId(R.id.submit_button))

    fun fillTitle(title: String) = apply {
        titleEditText.perform(ViewActions.typeText(title))
    }

    fun fillAmount(amount: String) = apply {
        amountEditText.perform(ViewActions.typeText(amount))
    }

    fun fillComment(comment: String) = apply {
        commentEditText.perform(ViewActions.typeText(comment))
    }

    fun pressSubmit() {
        submitButton.perform(click())
    }
}
