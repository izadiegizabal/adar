package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.NotEnoughInformationException
import xyz.izadi.adar.domain.repository.AccountsRepository

class FetchAccountWithTransactionsUseCase @Inject constructor(
    private val repository: AccountsRepository
) : UseCase<Int, Flow<Result<AccountWithTransactions>>> {

    @Throws(NotEnoughInformationException::class)
    override suspend fun invoke(param: Int?): Flow<Result<AccountWithTransactions>> = flow {
        emit(Result.Loading)
        runCatching {
            param?.let {
                repository.getAccountWithTransactions(param)
            } ?: throw NotEnoughInformationException("Must Specify Account Id")
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Error(it))
        }
    }
}