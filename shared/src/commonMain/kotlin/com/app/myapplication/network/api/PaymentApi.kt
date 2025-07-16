package com.app.myapplication.network.api

import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

open class PaymentApi(private val httpClient: HttpClient) {
    open suspend fun sendPayment(request: PaymentRequest): PaymentResponse {
        return httpClient.post("https://ihsan-static.onrender.com/payments") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
