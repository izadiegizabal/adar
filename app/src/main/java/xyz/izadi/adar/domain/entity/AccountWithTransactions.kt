package xyz.izadi.adar.domain.entity

interface AccountWithTransactions {
    val account: Account
    val transactions: List<Transaction>
}