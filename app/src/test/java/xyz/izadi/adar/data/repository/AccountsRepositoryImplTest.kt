package xyz.izadi.adar.data.repository

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.Instant
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.AccountWithTransactionsImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.local.room.dao.AccountsDao
import xyz.izadi.adar.data.local.room.dao.TransactionsDao
import xyz.izadi.adar.domain.entity.NoTransactionsException
import xyz.izadi.adar.domain.repository.AccountsRepository
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AccountsRepositoryImplTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockDataLoader: DataLoader

    @Mock
    private lateinit var mockAccountsDao: AccountsDao

    @Mock
    private lateinit var mockTransactionsDao: TransactionsDao

    private lateinit var sut: AccountsRepository

    /// Default sample data /////
    private val testAccounts = listOf(
        AccountImpl(id = 1, name = "Savings", institution = "UFJ", currency = "JPY", currentBalance = 1234.4, currentBalanceInBase = 1234.4)
    )

    private val testAccountWithTrans = AccountWithTransactionsImpl(
        account = testAccounts.first(),
        transactions = listOf(
            TransactionImpl(
                id = 1,
                accountId = testAccounts.first().id,
                amount = 123.4,
                categoryId = 4,
                date = Instant.parse("2017-05-24T00:00:00+09:00"),
                description = "This is a sample transaction"
            )
        )
    )
    private val testAccountId = 1
    ////////////////////////////

    @Before
    fun setup() {
        sut = AccountsRepositoryImpl(
            mockAccountsDao,
            mockTransactionsDao,
            mockDataLoader
        )
    }

    @Test
    fun fetch_account_list_json() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockAccountsDao.doWeHaveAccounts()).thenReturn(false)
        `when`(mockDataLoader.fetchAccounts()).thenReturn(testAccounts)
        `when`(mockAccountsDao.getAccounts()).thenReturn(testAccounts)

        // act
        val actual = sut.getAccounts()

        // assert
        verify(mockAccountsDao, times(1)).doWeHaveAccounts()
        verify(mockDataLoader, times(1)).fetchAccounts()
        verify(mockAccountsDao, times(1)).saveAccounts(testAccounts)
        verify(mockAccountsDao, times(1)).getAccounts()
        assertEquals(testAccounts, actual)
    }

    @Test
    fun fetch_account_list_json_error() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockAccountsDao.doWeHaveAccounts()).thenReturn(false)
        `when`(mockDataLoader.fetchAccounts()).thenThrow(SerializationException())

        // act
        val actual = runCatching {
            sut.getAccounts()
        }.getOrElse { it }

        // assert
        verify(mockAccountsDao, times(1)).doWeHaveAccounts()
        verify(mockDataLoader, times(1)).fetchAccounts()
        verify(mockAccountsDao, times(0)).saveAccounts(testAccounts)
        verify(mockAccountsDao, times(0)).getAccounts()
        assertTrue(actual is SerializationException)
    }

    @Test
    fun fetch_account_list_db() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockAccountsDao.doWeHaveAccounts()).thenReturn(true)
        `when`(mockAccountsDao.getAccounts()).thenReturn(testAccounts)

        // act
        val actual = sut.getAccounts()

        // assert
        verify(mockAccountsDao, times(1)).doWeHaveAccounts()
        verify(mockDataLoader, times(0)).fetchAccounts()
        verify(mockAccountsDao, times(0)).saveAccounts(anyList())
        verify(mockAccountsDao, times(1)).getAccounts()
        assertEquals(testAccounts, actual)
    }

    @Test
    fun fetch_account_with_transactions_json_valid_account() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockTransactionsDao.doWeHaveTransactionsForAccount(anyInt())).thenReturn(false)
        `when`(mockDataLoader.getTransactions(anyInt())).thenReturn(testAccountWithTrans.transactions)
        `when`(mockAccountsDao.getAccountWithTransactions(anyInt())).thenReturn(testAccountWithTrans)

        // act
        val actual = sut.getAccountWithTransactions(testAccountId)

        // assert
        verify(mockTransactionsDao, times(1)).doWeHaveTransactionsForAccount(testAccountId)
        verify(mockDataLoader, times(1)).getTransactions(anyInt())
        verify(mockTransactionsDao, times(1)).saveTransactions(testAccountWithTrans.transactions)
        verify(mockAccountsDao, times(1)).getAccountWithTransactions(testAccountId)
        assertEquals(testAccountWithTrans, actual)
    }


    @Test
    fun fetch_account_with_transactions_json_invalid_account() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockTransactionsDao.doWeHaveTransactionsForAccount(anyInt())).thenReturn(false)
        `when`(mockDataLoader.getTransactions(anyInt())).thenThrow(NoTransactionsException())

        // act
        val actual = runCatching {
            sut.getAccountWithTransactions(testAccountId)
        }.getOrElse {
            it
        }

        // assert
        verify(mockTransactionsDao, times(1)).doWeHaveTransactionsForAccount(testAccountId)
        verify(mockDataLoader, times(1)).getTransactions(testAccountId)
        verify(mockTransactionsDao, times(0)).saveTransactions(anyList())
        verify(mockAccountsDao, times(0)).getAccountWithTransactions(anyInt())
        assertTrue(actual is NoTransactionsException)
    }

    @Test
    fun fetch_account_with_transactions_db() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        `when`(mockTransactionsDao.doWeHaveTransactionsForAccount(anyInt())).thenReturn(true)
        `when`(mockAccountsDao.getAccountWithTransactions(anyInt())).thenReturn(testAccountWithTrans)

        // act
        val actual = sut.getAccountWithTransactions(testAccountId)

        // assert
        verify(mockTransactionsDao, times(1)).doWeHaveTransactionsForAccount(testAccountId)
        verify(mockDataLoader, times(0)).getTransactions(anyInt())
        verify(mockTransactionsDao, times(0)).saveTransactions(anyList())
        verify(mockAccountsDao, times(1)).getAccountWithTransactions(testAccountId)
        assertEquals(testAccountWithTrans, actual)
    }

    @Test
    fun delete_transaction() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        sut.deleteTransaction(1)

        // assert
        verify(mockTransactionsDao, times(1)).delete(1)
    }

    @Test
    fun add_transaction() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        sut.addTransactions(testAccountWithTrans.transactions)

        // assert
        verify(mockTransactionsDao, times(1)).saveTransactions(testAccountWithTrans.transactions)
    }
}