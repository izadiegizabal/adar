package xyz.izadi.adar.domain.usecase

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val state: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

fun Result<*>.runIfSuccess(enabled: Boolean = true, run: () -> Unit) {
    if (enabled && this is Result.Success) {
        run()
    } else if (!enabled) {
        run()
    }
}