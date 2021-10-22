package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.domain.repository.AccountsRepository

class DeleteTransactionUseCase @Inject constructor(
    private val repository: AccountsRepository
) : UseCase<Int, Flow<Result<Boolean>>> {

    @Throws(NotEnoughInformationException::class)
    override suspend fun invoke(param: Int?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        runCatching {
            param?.let { transactionId ->
                repository.deleteTransaction(transactionId)
                true
            } ?: throw NotEnoughInformationException("Must send an ID")
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Error(it))
        }
    }

}