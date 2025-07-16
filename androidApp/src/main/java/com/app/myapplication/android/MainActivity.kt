package com.app.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.myapplication.android.ui.PaymentAppScreen
import com.app.myapplication.di.sharedModule
import com.app.myapplication.viewmodel.PaymentViewModel
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(sharedModule)
        }

        setContent {
            val viewModel: PaymentViewModel = getKoin().get()
            PaymentAppScreen(viewModel)
        }
    }
}
