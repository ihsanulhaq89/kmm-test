package com.app.myapplication.repository

import com.app.myapplication.firestore.FirestoreService
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentTransaction
import com.app.myapplication.network.api.PaymentApi
import kotlinx.coroutines.flow.Flow

class PaymentRepository(
    private val apiService: PaymentApi,
    private val db: FirestoreService
) {
    suspend fun savePayment(payment: PaymentRequest): Boolean {
        val transaction = PaymentTransaction(
            id = "",
            recipientEmail = payment.recipientEmail,
            amount = payment.amount,
            currency = payment.currency
        )
        db.saveTransaction(transaction)
        return true
    }
    suspend fun postPayment(payment: PaymentRequest): Boolean {
        return try {
            val response = apiService.sendPayment(payment)
            println("API Success: ${response.recipientEmail}")
            true
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            false
        }
    }
    suspend fun getTransactions(): List<PaymentTransaction> {
        return db.getTransactions()
    }

    fun observeTransactions(): Flow<List<PaymentTransaction>> {
        return db.listenForTransactions()
    }

    fun validatePayment(payment: PaymentRequest): Boolean {
        return payment.recipientEmail.isNotBlank()
                && isValidEmail(payment.recipientEmail)
                && payment.amount > 0
                && (payment.currency == "USD" || payment.currency == "EUR")
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }
}
