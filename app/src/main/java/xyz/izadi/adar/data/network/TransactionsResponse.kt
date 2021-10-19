package xyz.izadi.adar.data.network

import kotlinx.serialization.Serializable
import xyz.izadi.adar.data.local.TransactionImpl

@Serializable
data class TransactionsResponse(val transactions: List<TransactionImpl>)