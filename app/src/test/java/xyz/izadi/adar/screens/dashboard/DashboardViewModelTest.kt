package xyz.izadi.adar.screens.dashboard

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.Instant
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyList
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.AccountWithTransactionsImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.domain.usecase.AddTransactionsUseCase
import xyz.izadi.adar.domain.usecase.DeleteTransactionUseCase
import xyz.izadi.adar.domain.usecase.FetchAccountWithTransactionsUseCase
import xyz.izadi.adar.domain.usecase.FetchAccountsUseCase
import xyz.izadi.adar.domain.usecase.Result
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var sut: DashboardViewModel

    @Mock
    private lateinit var mockFetchAccountsUseCase: FetchAccountsUseCase

    @Mock
    private lateinit var mockFetchAccountWithTransactionsUseCase: FetchAccountWithTransactionsUseCase

    @Mock
    private lateinit var mockDeleteTransactionUseCase: DeleteTransactionUseCase

    @Mock
    private lateinit var mockAddTransactionsUseCase: AddTransactionsUseCase

    /// Default sample data /////
    private val sampleAccount = AccountImpl(id = 1, name = "Savings", institution = "UFJ", currency = "JPY", currentBalance = 1234.4, currentBalanceInBase = 1234.4)
    private val testAccountWithTrans = AccountWithTransactionsImpl(
        account = sampleAccount,
        transactions = listOf(
            TransactionImpl(
                id = 1,
                accountId = 1,
                amount = 123.4,
                categoryId = 4,
                date = Instant.parse("2017-05-24T00:00:00+09:00"),
                description = "This is a sample transaction"
            )
        )
    )
    private val sampleTransaction = TransactionImpl(
        id = 1,
        accountId = 5,
        amount = 123.4,
        categoryId = 4,
        date = Instant.parse("2017-05-24T00:00:00+09:00"),
        description = "This is a sample transaction"
    )
    private val sampleAccounts = listOf(
        sampleAccount
    )
    ////////////////////////////

    @Before
    fun setup() {
        sut = DashboardViewModel(
            mockFetchAccountsUseCase,
            mockFetchAccountWithTransactionsUseCase,
            mockDeleteTransactionUseCase,
            mockAddTransactionsUseCase
        )
    }

    @Test
    fun expected_init() = coroutineTestRule.testDispatcher.runBlockingTest {
        // assert
        assertEquals(Result.Loading, sut.accounts.value)
        assertEquals(Result.Loading, sut.selectedAccountTransactions.value)
        assertEquals(0.0, sut.netWorth.value)
        verify(mockFetchAccountsUseCase, times(1)).invoke(any())
    }

    @Test
    fun fetch_accounts_success() = runBlocking {
        // arrange
        val expected = listOf(
            AccountImpl(id = 1, name = "Savings", institution = "UFJ", currency = "JPY", currentBalance = 1234.4, currentBalanceInBase = 1234.4)
        )
        `when`(mockFetchAccountsUseCase.invoke(any())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(expected))
            }
        )

        // act
        sut.fetchAccounts()

        // verify
        assertEquals(expected, (sut.accounts.value as Result.Success).state)
        assertEquals(expected.sumOf { it.currentBalanceInBase }, sut.netWorth.value)
    }

    @Test
    fun fetch_accounts_error() = runBlocking {
        // arrange
        val expected = SerializationException()
        `when`(mockFetchAccountsUseCase.invoke(any())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(expected))
            }
        )

        // act
        sut.fetchAccounts()

        // verify
        assertEquals(expected, (sut.accounts.value as Result.Error).exception)
        assertEquals(0.0, sut.netWorth.value)
    }

    @Test
    fun fetch_accounts_update_only_success() = runBlocking {
        // arrange
        `when`(mockFetchAccountsUseCase.invoke(any())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(SerializationException()))
            }
        )

        // act
        sut.fetchAccounts(updateOnlyIfSuccess = true)

        // verify
        assertTrue(sut.accounts.value is Result.Loading)
        assertEquals(0.0, sut.netWorth.value)
    }

    @Test
    fun select_accounts_success() = runBlocking {
        // arrange
        `when`(mockFetchAccountWithTransactionsUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(testAccountWithTrans))
            }
        )

        // act
        sut.selectAccount(1)

        // verify
        assertEquals(testAccountWithTrans, (sut.selectedAccountTransactions.value as Result.Success).state)
    }

    @Test
    fun select_account_error() = runBlocking {
        // arrange
        val expected = SerializationException()
        `when`(mockFetchAccountWithTransactionsUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(expected))
            }
        )

        // act
        sut.selectAccount(1)

        // verify
        assertEquals(expected, (sut.selectedAccountTransactions.value as Result.Error).exception)
    }

    @Test
    fun select_account_update_only_success() = runBlocking {
        // arrange
        `when`(mockFetchAccountWithTransactionsUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(SerializationException()))
            }
        )

        // act
        sut.selectAccount(1, updateOnlyIfSuccess = true)

        // verify
        assertTrue(sut.selectedAccountTransactions.value is Result.Loading)
    }

    @Test
    fun unselect_account() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        sut.unselectAccount()

        // verify
        assertTrue(sut.selectedAccountTransactions.value is Result.Loading)
    }

    @Test
    fun delete_transaction_success(): Unit = runBlocking {
        // arrange
        `when`(mockDeleteTransactionUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(true))
            }
        )
        `when`(mockFetchAccountWithTransactionsUseCase.invoke(anyInt())).thenReturn(flowOf(Result.Success(testAccountWithTrans)))
        `when`(mockFetchAccountsUseCase.invoke()).thenReturn(flowOf(Result.Success(sampleAccounts)))

        // act
        sut.deleteTransaction(sampleTransaction)

        // verify
        verify(mockDeleteTransactionUseCase).invoke(1)
        assertEquals(testAccountWithTrans, (sut.selectedAccountTransactions.value as Result.Success).state)
        assertEquals(sampleAccounts, (sut.accounts.value as Result.Success).state)
    }

    @Test
    fun delete_transaction_error() = runBlocking {
        // arrange
        `when`(mockDeleteTransactionUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(Exception()))
            }
        )

        // act
        sut.deleteTransaction(sampleTransaction)

        // verify
        verify(mockDeleteTransactionUseCase).invoke(1)
        assertTrue(sut.selectedAccountTransactions.value is Result.Loading)
        assertTrue(sut.accounts.value is Result.Loading)
    }

    @Test
    fun restore_transaction_success(): Unit = runBlocking {
        // arrange
        `when`(mockAddTransactionsUseCase.invoke(any())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(true))
            }
        )
        `when`(mockFetchAccountWithTransactionsUseCase.invoke(anyInt())).thenReturn(flowOf(Result.Success(testAccountWithTrans)))
        `when`(mockFetchAccountsUseCase.invoke()).thenReturn(flowOf(Result.Success(sampleAccounts)))

        // act
        sut.restoreTransaction(sampleTransaction)

        // verify
        verify(mockAddTransactionsUseCase).invoke(anyList())
        assertEquals(testAccountWithTrans, (sut.selectedAccountTransactions.value as Result.Success).state)
        assertEquals(sampleAccounts, (sut.accounts.value as Result.Success).state)
    }

    @Test
    fun restore_transaction_error() = runBlocking {
        // arrange
        `when`(mockAddTransactionsUseCase.invoke(any())).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(Exception()))
            }
        )

        // act
        sut.restoreTransaction(sampleTransaction)

        // verify
        verify(mockAddTransactionsUseCase).invoke(anyList())
        assertTrue(sut.selectedAccountTransactions.value is Result.Loading)
        assertTrue(sut.accounts.value is Result.Loading)
    }
}