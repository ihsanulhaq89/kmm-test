package com.app.myapplication.payment

import com.app.myapplication.mock.firestore.FakeFirestoreService
import com.app.myapplication.mock.network.FakeApiService
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.repository.PaymentRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PaymentValidationTest : StringSpec({

    val repository = PaymentRepository(apiService = FakeApiService(), db = FakeFirestoreService())

    "valid payment should pass validation" {
        val payment = PaymentRequest(
            recipientEmail = "user@example.com",
            amount = 100.0,
            currency = "USD"
        )
        repository.validatePayment(payment) shouldBe true
    }

    "invalid email should fail validation" {
        val payment = PaymentRequest(
            recipientEmail = "invalid-email",
            amount = 100.0,
            currency = "USD"
        )
        repository.validatePayment(payment) shouldBe false
    }

    "zero amount should fail validation" {
        val payment = PaymentRequest(
            recipientEmail = "user@example.com",
            amount = 0.0,
            currency = "USD"
        )
        repository.validatePayment(payment) shouldBe false
    }

    "negative amount should fail validation" {
        val payment = PaymentRequest(
            recipientEmail = "user@example.com",
            amount = -50.0,
            currency = "USD"
        )
        repository.validatePayment(payment) shouldBe false
    }

    "unsupported currency should fail validation" {
        val payment = PaymentRequest(
            recipientEmail = "user@example.com",
            amount = 50.0,
            currency = "GBP" // Only USD, EUR supported
        )
        repository.validatePayment(payment) shouldBe false
    }
})
