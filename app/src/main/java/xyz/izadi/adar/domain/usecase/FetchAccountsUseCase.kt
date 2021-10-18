package xyz.izadi.adar.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.repository.AccountsRepository

class FetchAccountsUseCase @Inject constructor(private val repository: AccountsRepository) : UseCase<Unit, Flow<Result<List<Account>>>> {

    override suspend fun invoke(param: Unit?): Flow<Result<List<Account>>> = withContext(Dispatchers.IO) {
        flow {
            emit(Result.Loading)
            runCatching {
                repository.getAccounts()
            }.onSuccess {
                emit(Result.Success(it))
            }.onFailure {
                emit(Result.Error(it))
            }
        }
    }
}