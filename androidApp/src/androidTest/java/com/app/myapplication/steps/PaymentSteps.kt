package com.app.myapplication.steps

import com.app.myapplication.domain.usecase.ProcessPaymentUseCase
import com.app.myapplication.mock.firestore.FakeFirestoreService
import com.app.myapplication.mock.network.FakeApiService
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.repository.PaymentRepository
import com.app.myapplication.viewmodel.PaymentViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import kotlinx.coroutines.runBlocking
import io.kotest.matchers.shouldBe

class PaymentSteps {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var paymentRequest: PaymentRequest
    private var apiShouldFail = false

    @Given("^a user enters recipient email \"([^\"]*)\", amount \"([^\"]*)\", and currency \"([^\"]*)\"$")
    fun a_user_enters_recipient_email_amount_and_currency(email: String, amount: String, currency: String) {
        paymentRequest = PaymentRequest(
            recipientEmail = email,
            amount = amount.toDouble(),
            currency = currency
        )
        val apiService = FakeApiService(shouldFail = apiShouldFail)
        val firestoreService = FakeFirestoreService()
        val repository = PaymentRepository(apiService, firestoreService)
        val processUseCase = ProcessPaymentUseCase(repository)
        val loadUseCase = LoadTransactionsUseCase(repository)

        viewModel = PaymentViewModel(processUseCase, loadUseCase)
    }

    @Given("^the API is configured to fail$")
    fun the_api_is_configured_to_fail() {
        apiShouldFail = true
    }

    @When("^they submit the payment$")
    fun they_submit_the_payment() = runBlocking {
        viewModel.sendPayment(paymentRequest)
    }

    @Then("^the payment should be processed successfully$")
    fun the_payment_should_be_processed_successfully() {
        viewModel.status.value shouldBe "Payment Sent"
    }

    @Then("^the payment should not be processed$")
    fun the_payment_should_not_be_processed() {
        viewModel.status.value shouldBe "Payment Failed: Simulated API failure"
    }

    @Then("^an error message \"([^\"]*)\" should be shown$")
    fun an_error_message_should_be_shown(expectedMessage: String) {
        viewModel.status.value shouldBe expectedMessage
    }

    @Then("^the transaction should be saved in Firestore$")
    fun the_transaction_should_be_saved_in_firestore() = runBlocking {
        val transactions = viewModel.transactions.value
        transactions.any { it.recipientEmail == paymentRequest.recipientEmail } shouldBe true
    }
}
