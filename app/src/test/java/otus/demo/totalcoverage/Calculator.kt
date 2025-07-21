package otus.demo.totalcoverage

import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    fun subtract(a: Int, b: Int): Int = a - b
}

class CalculatorBeforeTest {
    private lateinit var calc: Calculator

    @get:Rule
    val baseBeforeRule = BaseBeforeRule()

    @get:Rule
    val baseAfterRule = BaseAfterRule()

    @Before
    fun setup() {
        calc = Calculator()
        println("setup")
    }

    @Test
    fun `add sums`() {
        println("add sums")
        assertEquals(5, calc.add(2, 3))
    }

    @Test
    fun `subtract diffs`() {
        println("subtract diffs")
        assertEquals(1, calc.subtract(3, 2))
    }

    @After
    fun teardown() {
        println("teardown")
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            println("setupClass")
        }

        @JvmStatic
        @AfterClass
        fun teardownClass() {
            println("teardownClass")
        }
    }
}