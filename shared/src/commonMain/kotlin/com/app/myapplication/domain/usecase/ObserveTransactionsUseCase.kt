package com.app.myapplication.domain.usecase

import com.app.myapplication.model.PaymentTransaction
import com.app.myapplication.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow

class ObserveTransactionsUseCase(
    private val repository: PaymentRepository
) {
    operator fun invoke(): Flow<List<PaymentTransaction>> {
        return repository.observeTransactions()
    }
}
