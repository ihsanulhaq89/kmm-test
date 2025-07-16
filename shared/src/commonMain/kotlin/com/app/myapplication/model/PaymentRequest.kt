package com.app.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val recipientEmail: String,
    val amount: Double,
    val currency: String
)