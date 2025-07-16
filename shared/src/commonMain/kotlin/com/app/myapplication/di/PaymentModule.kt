package com.app.myapplication.di

import com.app.myapplication.domain.usecase.ObserveTransactionsUseCase
import com.app.myapplication.domain.usecase.ProcessPaymentUseCase
import com.app.myapplication.firestore.FirestoreService
import com.app.myapplication.firestore.FirestoreServiceImpl
import com.app.myapplication.network.api.PaymentApi
import com.app.myapplication.repository.PaymentRepository
import com.app.myapplication.viewmodel.PaymentViewModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
        }
    }
    single { PaymentApi(get()) }
    single<FirestoreService> { FirestoreServiceImpl() }
    single { PaymentRepository(get(), get()) }
    single { ProcessPaymentUseCase(get()) }
    single { ObserveTransactionsUseCase(get()) }

    factory {
        PaymentViewModel(
            processPaymentUseCase = get(),
            observeTransactionsUseCase = get()
        )
    }
}