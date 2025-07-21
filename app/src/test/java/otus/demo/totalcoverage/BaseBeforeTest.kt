package otus.demo.totalcoverage

import org.junit.After
import org.junit.Before
import org.junit.rules.ExternalResource

open class BaseBeforeTest {

    @Before
    fun before() {
        println("Setup")
    }
}

open class BaseAfterTest {

    @After
    fun after() {
        println("After test")
    }
}

class BaseBeforeRule : ExternalResource() {

    override fun before() {
        println("Before test")
    }
}

class BaseAfterRule : ExternalResource() {

    override fun after() {
        println("After test")
    }
}