package xyz.izadi.adar.domain.usecase

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.repository.AccountsRepositoryImpl
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeleteTransactionsUseCaseTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockRepository: AccountsRepositoryImpl

    private lateinit var sut: DeleteTransactionUseCase

    /// Default sample data /////
    private val sampleTransactionId = 1
    ////////////////////////////

    @Before
    fun setup() {
        sut = DeleteTransactionUseCase(mockRepository)
    }

    @Test
    fun delete_successful() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        val actual = sut.invoke(sampleTransactionId).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Success(true)),
            actual
        )
    }

    @Test
    fun delete_error() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        val exception = SerializationException()
        `when`(mockRepository.deleteTransaction(anyInt())).thenThrow(exception)

        // act
        val actual = sut.invoke(sampleTransactionId).toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Error(exception)),
            actual
        )
    }

    @Test
    fun delete_null() = coroutineTestRule.testDispatcher.runBlockingTest {
        // act
        val actual = sut.invoke().toList()

        // assert
        assertEquals(2, actual.size)
        assertTrue(actual.first() is Result.Loading)
        assertTrue((actual[1] as Result.Error).exception is NotEnoughInformationException)
    }
}