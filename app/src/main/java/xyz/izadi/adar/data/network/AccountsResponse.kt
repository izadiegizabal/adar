package xyz.izadi.adar.data.network

import kotlinx.serialization.Serializable
import xyz.izadi.adar.data.local.AccountImpl

@Serializable
data class AccountsResponse(val accounts: List<AccountImpl>)