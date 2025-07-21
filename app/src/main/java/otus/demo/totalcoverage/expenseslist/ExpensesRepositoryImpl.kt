package otus.demo.totalcoverage.expenseslist

import io.reactivex.Single
import otus.demo.totalcoverage.addexpense.AddExpenseRequest
import otus.demo.totalcoverage.baseexpenses.Category
import otus.demo.totalcoverage.baseexpenses.Expense
import otus.demo.totalcoverage.baseexpenses.ExpenseResponse
import otus.demo.totalcoverage.baseexpenses.ExpensesService
import otus.demo.totalcoverage.utils.NeedsTesting
import javax.inject.Inject

@NeedsTesting
class ExpensesRepositoryImpl @Inject constructor(
    private val expensesService: ExpensesService,
    private val expensesMapper: ExpensesMapper
) : ExpensesRepository {

    override suspend fun getExpenses(): List<Expense> {
        return expensesService.getExpenses()
            .map { expensesMapper.map(it) }
    }
}

interface ExpensesRepository {

    suspend fun getExpenses(): List<Expense>
}

class Test {

    fun test() {
        val repository: ExpensesRepository = ExpensesRepositoryImpl(
            expensesService = StubExpensesService(),
            expensesMapper = ExpensesMapper()
        )
    }
}

class StubExpensesService : ExpensesService {

    var countCallGetExpenses = 0
        private set

    override suspend fun getExpenses(): List<ExpenseResponse> {
        countCallGetExpenses++
        return listOf(
            ExpenseResponse(
                1,
                "Some food",
                Category.FOOD,
                "Some grocery shop",
                1200L,
                1624345281000L
            ),
            ExpenseResponse(1, "Some food", Category.BARS, "Some bar", 3000L, 1624345281000L),
        )
    }

    override fun addExpense(addExpenseRequest: AddExpenseRequest): Single<ExpenseResponse> {
        TODO("Not yet implemented")
    }
}
