package xyz.izadi.adar.domain.usecase

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.Instant
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.AccountWithTransactionsImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.repository.AccountsRepositoryImpl
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.domain.usecase.Result
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchAccountWithTransactionsUseCaseTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockRepository: AccountsRepositoryImpl

    private lateinit var sut: FetchAccountWithTransactionsUseCase

    /// Default sample data /////

    private val sampleAccountWithTransactions = AccountWithTransactionsImpl(
        account = AccountImpl(id = 1, name = "Savings", institution = "UFJ", currency = "JPY", currentBalance = 1234.4, currentBalanceInBase = 1234.4),
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
    ////////////////////////////

    @Before
    fun setup() {
        sut = FetchAccountWithTransactionsUseCase(mockRepository)
    }

    @Test
    fun fetch_successful() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        Mockito.`when`(mockRepository.getAccountWithTransactions(anyInt())).thenReturn(sampleAccountWithTransactions)

        // act
        val actual = sut.invoke(3).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Success(sampleAccountWithTransactions)),
            actual
        )
    }

    @Test
    fun fetch_error() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        val exception = SerializationException()
        Mockito.`when`(mockRepository.getAccountWithTransactions(anyInt())).thenThrow(exception)

        // act
        val actual = sut.invoke(3).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Error(exception)),
            actual
        )
    }

    @Test
    fun fetch_null() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        val actual = sut.invoke().toList()

        // assert
        assertEquals(2, actual.size)
        assertTrue(actual.first() is Result.Loading)
        assertTrue((actual[1] as Result.Error).exception is NotEnoughInformationException)
    }
}