package com.app.myapplication.mock.network


import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentResponse
import com.app.myapplication.network.api.PaymentApi
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay

class FakeApiService : PaymentApi(httpClient = HttpClient()) { // No real HttpClient needed for mock

    var shouldFail: Boolean = false

    override suspend fun sendPayment(request: PaymentRequest): PaymentResponse {
        delay(100) // simulate network latency

        return if (shouldFail) {
            throw Exception("Simulated API failure")
        } else {
            PaymentResponse(
                recipientEmail = request.recipientEmail,
                amount = request.amount,
                currency = request.currency
            )
        }
    }
}
