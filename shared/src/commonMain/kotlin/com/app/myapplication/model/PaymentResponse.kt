package com.app.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    // val transactionId: String,
    val recipientEmail: String,
    val amount: Double,
    val currency: String
)