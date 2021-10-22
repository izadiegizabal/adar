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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.repository.AccountsRepositoryImpl
import xyz.izadi.adar.testutils.CoroutineTestRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchAccountsUseCaseTest : TestCase() {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockRepository: AccountsRepositoryImpl

    private lateinit var sut: FetchAccountsUseCase

    /// Default sample data /////
    private val sampleAccounts = listOf(
        AccountImpl(id = 1, name = "Savings", institution = "UFJ", currency = "JPY", currentBalance = 1234.4, currentBalanceInBase = 1234.4)
    )
    ////////////////////////////

    @Before
    fun setup() {
        sut = FetchAccountsUseCase(mockRepository)
    }

    @Test
    fun fetch_successful() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        Mockito.`when`(mockRepository.getAccounts()).thenReturn(sampleAccounts)

        // act
        val actual = sut.invoke().toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Success(sampleAccounts)),
            actual
        )
    }

    @Test
    fun fetch_error() = coroutineTestRule.testDispatcher.runBlockingTest {
        // arrange
        val exception = SerializationException()
        Mockito.`when`(mockRepository.getAccounts()).thenThrow(exception)

        // act
        val actual = sut.invoke().toList()

        // assert
        assertEquals(
            listOf(Result.Loading, Result.Error(exception)),
            actual
        )
    }
}