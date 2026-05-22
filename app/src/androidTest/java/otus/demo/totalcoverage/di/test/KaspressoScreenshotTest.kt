package otus.demo.totalcoverage.di.test

import android.Manifest
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import otus.demo.totalcoverage.ContainerActivity

/**
 * Альтернатива связке adbserver + hackPermissions из урока: для скриншотов достаточно runtime-прав
 * через [GrantPermissionRule] там, где разрешены legacy storage-права.
 */
class KaspressoScreenshotTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ContainerActivity::class.java)

    @get:Rule
    val storagePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

    @Test
    fun testStatsAfterFab() = run {
        step("Press FAB") {
            ExpensesScreen {
                fab { click() }
            }
        }
        step("Fill expense 1") {
            ExpenseScreen {
                title { typeText("Test 123") }
                amount { typeText("10") }
                comment { typeText("Test comment") }
                submit { click() }
            }
        }
        step("Take and check screenshot") {
            flakySafely(timeoutMs = 5000) {
                device.screenshots.takeAndApply("some_test") {
                    Assert.assertTrue("Screenshot file should exist", exists())
                }
            }
        }
    }
}
