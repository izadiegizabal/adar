package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.repository.AccountsRepository

class FetchAccountWithTransactionsUseCase @Inject constructor(
    private val repository: AccountsRepository
) : UseCase<Int, Flow<Result<AccountWithTransactions>>> {

    override suspend fun invoke(param: Int?): Flow<Result<AccountWithTransactions>> = flow {
        emit(Result.Loading)
        runCatching {
            param?.let {
                repository.getAccountWithTransactions(param)
            } ?: throw Throwable("Must Specify Account Id") // TODO: create custom throwable
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Error(it))
        }
    }
}