package com.app.myapplication.viewmodel

import com.app.myapplication.domain.usecase.ObserveTransactionsUseCase
import com.app.myapplication.domain.usecase.ProcessPaymentUseCase
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentTransaction
import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val observeTransactionsUseCase: ObserveTransactionsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val _transactions = MutableStateFlow<List<PaymentTransaction>>(emptyList())
    val transactions: StateFlow<List<PaymentTransaction>> = _transactions

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    init {
        loadTransactions()
    }

    fun sendPayment(payment: PaymentRequest) {
        viewModelScope.launch {
            _status.value = "Sending payment…"
            val result = processPaymentUseCase(payment)
            result
                .onSuccess {
                    _status.value = "Payment Sent"
                    loadTransactions() // Refresh transactions
                }
                .onFailure { e ->
                    _status.value = "Payment Failed: ${e.message}"
                }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            observeTransactionsUseCase()
                .collect { transactions ->
                    _transactions.value = transactions
                    _status.value = "Transactions loaded"
                }
        }
    }
}

