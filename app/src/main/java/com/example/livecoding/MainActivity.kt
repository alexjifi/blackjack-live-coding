package com.example.livecoding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.livecoding.di.ServiceLocator
import com.example.livecoding.presentation.cards.CardsRoute
import com.example.livecoding.presentation.cards.CardsViewModelFactory
import com.example.livecoding.ui.theme.LiveCodingTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<com.example.livecoding.presentation.cards.CardsViewModel> {
        CardsViewModelFactory(ServiceLocator.provideDrawOpeningHandUseCase())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveCodingTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CardsRoute(viewModel = viewModel)
                }
            }
        }
    }
}
