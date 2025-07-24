package otus.demo.totalcoverage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UsersMviViewModel(
    private val getUsers: GetUsersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun processIntent(intent: UiIntent) {
        when (intent) {
            UiIntent.Load -> loadUsers()
        }
    }

    private fun loadUsers() = viewModelScope.launch {
        _state.value = UiState.Loading
        try {
            // TODO: Dispacher?
            val list = getUsers()
            _state.value = UiState.Success(list)
        } catch (e: Throwable) {
            _state.value = UiState.Error(e)
        }
    }
}

sealed interface UiIntent {
    object Load : UiIntent
}

sealed interface UiState {
    object Loading        : UiState
    data class Success(val users: List<User>) : UiState
    data class Error(val e: Throwable)   : UiState
}

data class User(val name: String)
interface GetUsersUseCase {
    suspend operator fun invoke(): List<User>
}

class TestMVI {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val mockUseCase: GetUsersUseCase = mock()
    private lateinit var vm: UsersMviViewModel

    @Before
    fun setup() {
        vm = UsersMviViewModel(mockUseCase)
    }

    @Test
    fun `load intent emits Success state`() = runTest {
        whenever(mockUseCase()).thenReturn(listOf(User("Bob")))

        vm.processIntent(UiIntent.Load)
        val result = vm.state.first()

        assertTrue(result is UiState.Success)
    }
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(desc: Description) =
        Dispatchers.setMain(dispatcher)
    override fun finished(desc: Description) =
        Dispatchers.resetMain()
}