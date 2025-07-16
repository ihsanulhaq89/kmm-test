package com.app.myapplication.payment

import com.app.myapplication.mock.firestore.FakeFirestoreService
import com.app.myapplication.mock.network.FakeApiService
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.repository.PaymentRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class PaymentRepositoryTest : StringSpec({

    val fakeApi = FakeApiService()
    val fakeDb = FakeFirestoreService()
    val repository = PaymentRepository(fakeApi, fakeDb)

    "savePayment should store transaction in Firestore" {
        val payment = PaymentRequest("user@example.com", 100.0, "USD")

        runTest {
            val result = repository.savePayment(payment)

            result shouldBe true
            fakeDb.getTransactions().size shouldBe 1
            fakeDb.getTransactions().first().recipientEmail shouldBe "user@example.com"
        }
    }

    "postPayment should return true on API success" {
        val payment = PaymentRequest("api@success.com", 50.0, "EUR")

        runTest {
            val result = repository.postPayment(payment)

            result shouldBe true
        }
    }

    "postPayment should return false on API failure" {
        fakeApi.shouldFail = true
        val payment = PaymentRequest("api@fail.com", 75.0, "USD")

        runTest {
            val result = repository.postPayment(payment)

            result shouldBe false
        }
    }

    "getTransactions should return all saved transactions" {
        val fakeDb2 = FakeFirestoreService()
        val repository2 = PaymentRepository(fakeApi, fakeDb2)
        runTest {
            val payment1 = PaymentRequest("a@b.com", 10.0, "USD")
            val payment2 = PaymentRequest("b@c.com", 20.0, "EUR")

            repository2.savePayment(payment1)
            repository2.savePayment(payment2)

            val transactions = repository2.getTransactions()

            transactions.size shouldBe 2
            transactions[0].recipientEmail shouldBe "a@b.com"
            transactions[1].recipientEmail shouldBe "b@c.com"
        }
    }

    "validatePayment should return true for valid payment" {
        val payment = PaymentRequest("valid@email.com", 100.0, "USD")

        val isValid = repository.validatePayment(payment)

        isValid shouldBe true
    }

    "validatePayment should return false for invalid email" {
        val payment = PaymentRequest("invalid-email", 100.0, "USD")

        val isValid = repository.validatePayment(payment)

        isValid shouldBe false
    }

    "validatePayment should return false for amount <= 0" {
        val payment = PaymentRequest("user@example.com", 0.0, "USD")

        val isValid = repository.validatePayment(payment)

        isValid shouldBe false
    }

    "validatePayment should return false for unsupported currency" {
        val payment = PaymentRequest("user@example.com", 50.0, "GBP")

        val isValid = repository.validatePayment(payment)

        isValid shouldBe false
    }
})