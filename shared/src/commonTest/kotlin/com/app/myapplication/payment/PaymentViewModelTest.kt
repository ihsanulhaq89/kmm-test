package com.app.myapplication.payment

import com.app.myapplication.domain.usecase.ObserveTransactionsUseCase
import com.app.myapplication.domain.usecase.ProcessPaymentUseCase
import com.app.myapplication.mock.firestore.FakeFirestoreService
import com.app.myapplication.mock.network.FakeApiService
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentTransaction
import com.app.myapplication.repository.PaymentRepository
import com.app.myapplication.viewmodel.PaymentViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: PaymentViewModel
    lateinit var fakeFirestore: FakeFirestoreService

    val initialFakeTransactions = arrayListOf(
        PaymentTransaction("1", "user1@example.com", 100.0, "USD"),
        PaymentTransaction("2", "user2@example.com", 200.0, "EUR")
    )

    beforeSpec {
        Dispatchers.setMain(testDispatcher)

        fakeFirestore = FakeFirestoreService(initialFakeTransactions)
        val fakeApi = FakeApiService()
        val repository = PaymentRepository(fakeApi, fakeFirestore)

        val processUseCase = ProcessPaymentUseCase(repository)
        val observeUseCase = ObserveTransactionsUseCase(repository)

        viewModel = PaymentViewModel(
            processPaymentUseCase = processUseCase,
            observeTransactionsUseCase = observeUseCase,
            dispatcher = testDispatcher
        )
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    "initial load should populate transactions" {
        testDispatcher.scheduler.advanceUntilIdle()

        val transactions = viewModel.transactions.value
        transactions.shouldNotBeEmpty()
        transactions.size shouldBe 2
        transactions.first().recipientEmail shouldBe "user1@example.com"
    }

    "sendPayment should add a new transaction and update status" {
        val newPayment = PaymentRequest("newuser@example.com", 150.0, "USD")
        viewModel.sendPayment(newPayment)
        testDispatcher.scheduler.advanceUntilIdle()

        val status = viewModel.status.value
        status shouldContain "Transactions loaded"

        val transactions = viewModel.transactions.value
        transactions.size shouldBe 3
        transactions.last().recipientEmail shouldBe "newuser@example.com"
        transactions.last().amount shouldBe 150.0
    }
})
