package com.app.myapplication.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.myapplication.model.PaymentRequest
import com.app.myapplication.model.PaymentTransaction
import com.app.myapplication.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAppScreen(viewModel: PaymentViewModel? = null) {
    var showHistory by remember { mutableStateOf(false) }
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var currency by remember { mutableStateOf("USD") }

    val transactions = viewModel?.transactions?.collectAsState(emptyList())?.value ?: emptyList()
    val status = viewModel?.status?.collectAsState()?.value ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (showHistory) "📜 Transaction History" else "💸 KMP Payment App",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showHistory = !showHistory }) {
                        Icon(
                            imageVector = if (showHistory) Icons.Default.Home else Icons.Default.DateRange,
                            contentDescription = if (showHistory) "Go to Payment" else "View History"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                if (showHistory) {
                    TransactionHistory(transactions)
                } else {
                    PaymentForm(
                        recipient = recipient,
                        onRecipientChange = { recipient = it },
                        amount = amount,
                        onAmountChange = { amount = it },
                        currency = currency,
                        onCurrencyChange = { currency = it },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        onSendClick = {
                            viewModel?.sendPayment(
                                PaymentRequest(
                                    recipientEmail = recipient,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    currency = currency
                                )
                            )
                        },
                        status = status
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentForm(
    recipient: String,
    onRecipientChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    currency: String,
    onCurrencyChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSendClick: () -> Unit,
    status: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = recipient,
            onValueChange = onRecipientChange,
            label = { Text("Recipient Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(!expanded) }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = currency,
                onValueChange = {},
                label = { Text("Currency") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                listOf("USD", "EUR").forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onCurrencyChange(selectionOption)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        Button(
            onClick = onSendClick,
            modifier = Modifier
                .align(Alignment.End)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Payment")
        }

        if (status.isNotEmpty()) {
            Text(
                text = status,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TransactionHistory(transactions: List<PaymentTransaction>) {
    if (transactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No transactions yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(transactions) { txn ->
                TransactionCard(txn)
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: PaymentTransaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(transaction.recipientEmail, fontWeight = FontWeight.Bold)
                Text("${transaction.amount} ${transaction.currency}", fontSize = 14.sp)
            }
            Text(
                text = transaction.timestamp.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPaymentAppScreen() {
    PaymentAppScreen()
}
