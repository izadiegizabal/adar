package xyz.izadi.adar.domain.entity

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Route {
    val path: String
    fun getFullPath(): String
    fun getArguments(): List<NamedNavArgument>
}

sealed class Routes(override val path: String) : Route {
    override fun getFullPath() = path
    override fun getArguments() = listOf<NamedNavArgument>()


    object Dashboard : Routes(path = "dashboard")

    class Account(val id: Int? = null, val name: String? = null) : Routes(path = "account") {
        override fun getFullPath() = "$path/${id ?: "{$ARGUMENT_ID}"}/${name ?: "{$ARGUMENT_NAME}"}"
        override fun getArguments() = listOf(
            navArgument(ARGUMENT_ID) { type = NavType.IntType },
            navArgument(ARGUMENT_NAME) { type = NavType.StringType }
        )

        companion object {
            const val ARGUMENT_NAME = "name"
            const val ARGUMENT_ID = "id"
        }
    }
}