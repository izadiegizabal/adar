package xyz.izadi.adar.domain.entity

class NoTransactionsException(override val message: String = "No transactions for this account") : Exception()
class NotEnoughInformationException(override val message: String = "Not enough information for performing this"): Exception()