package xyz.izadi.adar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.izadi.adar.data.local.TransactionImpl.Companion.TABLE_NAME
import xyz.izadi.adar.domain.entity.Transaction

@Entity(tableName = TABLE_NAME)
@Serializable
data class TransactionImpl(
    @SerialName("account_id")
    override val accountId: Int,
    override val amount: Double,
    @SerialName("category_id")
    override val categoryId: Int,
    override val date: Instant,
    override val description: String,
    @PrimaryKey
    override val id: Int
) : Transaction {
    companion object {
        const val TABLE_NAME = "transactions"
    }
}