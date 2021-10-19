package xyz.izadi.adar.data.local

import androidx.room.Embedded
import androidx.room.Relation
import xyz.izadi.adar.domain.entity.AccountWithTransactions

data class AccountWithTransactionsImpl(
    @Embedded
    override val account: AccountImpl,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    override val transactions: List<TransactionImpl>
) : AccountWithTransactions