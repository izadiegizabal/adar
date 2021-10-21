package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.repository.AccountsRepository

class DeleteTransactionUseCase @Inject constructor(
    private val repository: AccountsRepository
) : UseCase<Int, Flow<Result<Boolean>>> {
    override suspend fun invoke(param: Int?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        runCatching {
            param?.let { transactionId ->
                repository.deleteTransaction(transactionId)
                true
            } ?: throw Exception("Must send an ID")
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Error(it))
        }
    }

}