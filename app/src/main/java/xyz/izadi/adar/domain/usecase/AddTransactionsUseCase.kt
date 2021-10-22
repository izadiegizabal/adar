package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.domain.repository.AccountsRepository

class AddTransactionsUseCase @Inject constructor(
    private val repository: AccountsRepository
) : UseCase<List<Transaction>, Flow<Result<Boolean>>> {

    @Throws(NotEnoughInformationException::class)
    override suspend fun invoke(param: List<Transaction>?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        runCatching {
            param?.let { transactions ->
                repository.addTransactions(transactions)
                true
            } ?: throw NotEnoughInformationException("Must send a transaction")
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Error(it))
        }
    }
}