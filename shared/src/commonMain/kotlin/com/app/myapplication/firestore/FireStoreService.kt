package com.app.myapplication.firestore

import com.app.myapplication.model.PaymentTransaction
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FirestoreService {
    suspend fun saveTransaction(transaction: PaymentTransaction)
    suspend fun getTransactions(): List<PaymentTransaction>
    fun listenForTransactions(): Flow<List<PaymentTransaction>>
}

class FirestoreServiceImpl: FirestoreService {

    private val db = Firebase.firestore
    private val transactionsCollection = db.collection("transactions")

    override suspend fun saveTransaction(transaction: PaymentTransaction) {
        transactionsCollection.add(transaction)
    }

    override suspend fun getTransactions(): List<PaymentTransaction> {
        val snapshot = transactionsCollection.get()
        return snapshot.documents.map { it.data<PaymentTransaction>() }
    }

    override fun listenForTransactions(): Flow<List<PaymentTransaction>> {
        return transactionsCollection.snapshots
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    doc.data<PaymentTransaction>().copy(id = doc.id)
                }
            }
    }
}