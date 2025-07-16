package com.app.myapplication.model
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class PaymentTransaction(
    val id: String = "",
    val recipientEmail: String,
    val amount: Double,
    val currency: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)
