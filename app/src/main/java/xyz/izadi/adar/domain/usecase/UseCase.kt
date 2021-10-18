package xyz.izadi.adar.domain.usecase

interface UseCase<T, K> {
    suspend fun invoke(param: T? = null): K
}