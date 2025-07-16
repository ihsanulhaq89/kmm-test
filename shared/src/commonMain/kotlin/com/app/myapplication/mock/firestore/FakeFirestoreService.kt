package com.app.myapplication.mock.firestore

import com.app.myapplication.firestore.FirestoreService
import com.app.myapplication.model.PaymentTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeFirestoreService(
    initialTransactions: List<PaymentTransaction> = emptyList()
) : FirestoreService {

    private val transactions = mutableListOf<PaymentTransaction>().apply {
        addAll(initialTransactions)
    }
    private val transactionsFlow = MutableStateFlow(transactions.toList())

    override suspend fun saveTransaction(transaction: PaymentTransaction) {
        transactions.add(transaction)
        transactionsFlow.value = transactions.toList()
    }

    override suspend fun getTransactions(): List<PaymentTransaction> {
        return transactions.toList()
    }

    override fun listenForTransactions(): Flow<List<PaymentTransaction>> {
        return transactionsFlow.asStateFlow()
    }
}
