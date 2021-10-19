package xyz.izadi.adar.domain.entity

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val state: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}