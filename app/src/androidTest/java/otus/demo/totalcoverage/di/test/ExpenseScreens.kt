package otus.demo.totalcoverage.di.test

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import otus.demo.totalcoverage.R
import otus.demo.totalcoverage.addexpense.AddExpenseFragment
import otus.demo.totalcoverage.expenseslist.ExpensesFragment

object ExpensesScreen : KScreen<ExpensesScreen>() {
    override val layoutId: Int? = R.layout.expenses_list_layout
    override val viewClass: Class<*>? = ExpensesFragment::class.java

    val fab = KView { withId(R.id.add_expense_fab) }
    val emptyText = KTextView { withId(R.id.empty_text) }

    val list = KRecyclerView(
        builder = { withId(R.id.expenses_recycler) },
        itemTypeBuilder = {
            itemType { ListItem(it) }
        },
    )

    class ListItem(parent: Matcher<View>) : KRecyclerItem<ListItem>(parent) {
        val name = KTextView(parent) { withId(R.id.tv_name) }
        val amount = KTextView(parent) { withId(R.id.tv_amount) }
        val comment = KTextView(parent) { withId(R.id.tv_comment) }
    }
}

/**
 * Экран добавления траты ([R.layout.add_expenses_layout]); в материале курса назывался ExpenseScreen.
 */
object ExpenseScreen : KScreen<ExpenseScreen>() {
    override val layoutId: Int? = R.layout.add_expenses_layout
    override val viewClass: Class<*>? = AddExpenseFragment::class.java

    val title = KEditText { withId(R.id.title_edittext) }
    val amount = KEditText { withId(R.id.amount_edittext) }
    val comment = KEditText { withId(R.id.comment_edittext) }
    val submit = KView { withId(R.id.submit_button) }
}
