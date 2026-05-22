package otus.demo.totalcoverage.di.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity
import otus.demo.totalcoverage.R

// Для работы device.phone см. adbserver-desktop.jar и документацию Kaspresso.
class KaspressoAdbDeviceTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Test
    fun testPhoneCall() = before {
        // java -jar adbserver-desktop.jar
    }.after {
        runCatching { device.phone.cancelCall("123123123") }
    }.run {
        onView(withId(R.id.add_expense_fab)).perform(click())
        val phone = "123123123"

        step("emulate call") {
            runCatching {
                device.phone.emulateCall(phone)
                Thread.sleep(7000)
                device.phone.cancelCall(phone)
            }
        }

        step("fill expense 2") {
            ExpenseScreen {
                title { typeText(phone) }
                amount { typeText("10") }
                comment { typeText("Test comment") }
                submit { click() }
            }
        }

        flakySafely(timeoutMs = 3000) {
            step("Check expenses") {
                ExpensesScreen {
                    list {
                        childAt<ExpensesScreen.ListItem>(0) {
                            name.hasText(phone)
                            amount.hasText("10 ₽")
                            comment.hasText("Test comment")
                        }
                    }
                    Assert.assertEquals(1, list.getSize())
                }
            }
        }
    }
}
