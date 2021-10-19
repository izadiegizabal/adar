package xyz.izadi.adar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.serialization.Serializable
import xyz.izadi.adar.data.local.TransactionImpl.Companion.TABLE_NAME
import xyz.izadi.adar.domain.entity.Transaction

@Entity(tableName = TABLE_NAME)
@Serializable
data class TransactionImpl(
    override val accountId: Int,
    override val amount: Double,
    override val categoryId: Int,
    @Serializable(with = InstantComponentSerializer::class)
    override val date: Instant,
    override val description: String,
    @PrimaryKey
    override val id: Int
) : Transaction {
    companion object {
        const val TABLE_NAME = "transactions"
    }
}