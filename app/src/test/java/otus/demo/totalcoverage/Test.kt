package otus.demo.totalcoverage

import org.junit.Rule
import org.junit.Test

class Tests2 {

    @get:Rule
    val baseBeforeRule = BaseBeforeRule()

    @get:Rule
    val baseAfterRule = BaseAfterRule()

    @Test
    fun `add sums`() {
        println("add sums")
    }
}