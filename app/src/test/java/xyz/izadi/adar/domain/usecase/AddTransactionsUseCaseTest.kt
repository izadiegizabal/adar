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
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.repository.AccountsRepositoryImpl
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddTransactionsUseCaseTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockRepository: AccountsRepositoryImpl

    private lateinit var sut: AddTransactionsUseCase

    /// Default sample data /////
    private val sampleTransactions = listOf(
        TransactionImpl(
            id = 1,
            accountId = 1,
            amount = 123.4,
            categoryId = 4,
            date = Instant.parse("2017-05-24T00:00:00+09:00"),
            description = "This is a sample transaction"
        )
    )
    ////////////////////////////

    @Before
    fun setup() {
        sut = AddTransactionsUseCase(mockRepository)
    }

    @Test
    fun add_successful() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        val actual = sut.invoke(sampleTransactions).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Success(true)),
            actual
        )
    }

    @Test
    fun add_error() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        val exception = SerializationException()
        `when`(mockRepository.addTransactions(anyList())).thenThrow(exception)

        // act
        val actual = sut.invoke(sampleTransactions).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Error(exception)),
            actual
        )
    }

    @Test
    fun add_null_transaction() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        val actual = sut.invoke().toList()

        // assert
        assertEquals(2, actual.size)
        assertTrue(actual.first() is Result.Loading)
        assertTrue((actual[1] as Result.Error).exception is NotEnoughInformationException)
    }
}