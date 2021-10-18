package xyz.izadi.adar.data.local

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.serialization.Serializable
import xyz.izadi.adar.domain.entity.Transaction

@Serializable
data class TransactionImpl(
    override val accountId: Int,
    override val amount: Double,
    override val categoryId: Int,
    @Serializable(with = InstantComponentSerializer::class)
    override val date: Instant,
    override val description: String,
    override val id: Int
) : Transaction