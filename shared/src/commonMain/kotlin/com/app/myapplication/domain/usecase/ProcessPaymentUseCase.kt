package com.app.myapplication.domain.usecase

import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.repository.PaymentRepository

open class ProcessPaymentUseCase(
    private val repository: PaymentRepository
) {
    /**
     * Processes a payment by validating, sending to API, and saving to Firestore.
     * @param paymentRequest The payment details entered by the user.
     * @return Result<Boolean> Success = true, Failure = false with exception
     */
    open suspend operator fun invoke(paymentRequest: PaymentRequest): Result<Boolean> {
        return try {
            if (!repository.validatePayment(paymentRequest)) {
                return Result.failure(IllegalArgumentException("Invalid payment details"))
            }

            val apiResult = repository.postPayment(paymentRequest)

            if (apiResult) {
                repository.savePayment(paymentRequest)
                Result.success(apiResult)
            } else {
                Result.failure(Exception())
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}