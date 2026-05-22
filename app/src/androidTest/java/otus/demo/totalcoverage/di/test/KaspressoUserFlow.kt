package otus.demo.totalcoverage.di.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.R

class KaspressoUserFlow : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Test
    fun testFirstFeature() = run {
//        onView(withId(R.id.empty_text))
//            .check(matches(withText("No expenses :)")))

        step("simple test") {
            ExpensesScreen {
                emptyText {
                    hasText("No expenses :)")
                }
            }
        }
        step("click on fab take screenshot") {
            ExpensesScreen {
                fab {
                    click()
                    testLogger.i("I am testLogger")
                    device.screenshots.take("Additional_screenshot")
                    // /storage/emulated/0/Documents/screenshots/<package>.KaspressoUserFlow/testFirstFeature/Additional_screenshot.png
                }
            }
        }
        step("Fill expense 1") {
            ExpenseScreen {
                title { typeText("Test") }
                amount { typeText("10") }
                comment { typeText("Test comment") }
                submit { click() }
            }
        }
        step("click on fab") {
            ExpensesScreen {
                fab { click() }
            }
        }
        step("Fill expense 2") {
            ExpenseScreen {
                title { typeText("Test") }
                amount { typeText("10") }
                comment { typeText("Test comment") }
                submit { click() }
            }
        }
        flakySafely(timeoutMs = 5000) {
            step("Check expenses") {
                ExpensesScreen {
                    list {
                        childAt<ExpensesScreen.ListItem>(0) {
                            name.hasText("Test")
                            amount.hasText("10 ₽")
                            comment.hasText("Test comment")
                        }
                    }
                    Assert.assertEquals(2, list.getSize())
                }
            }
        }
    }
}
