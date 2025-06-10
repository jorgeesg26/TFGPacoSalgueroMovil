package com.example.tfg_movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tfg_movil.model.authentication.classes.AuthRepository
import com.example.tfg_movil.model.customer.CustomerRepository
import com.example.tfg_movil.model.navigation.AppNavigation
import com.example.tfg_movil.model.navigation.NavigationDrawer
import com.example.tfg_movil.model.services.ServiceRepository
import com.example.tfg_movil.ui.theme.TFG_MovilTheme
import com.example.tfg_movil.viewmodel.ViewModelAuth
import com.example.tfg_movil.viewmodel.ViewModelCustomer
import com.example.tfg_movil.viewmodel.ViewModelService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val serviceViewModel: ViewModelService = viewModel {
                ViewModelService(ServiceRepository())
            }
            val customerViewModel: ViewModelCustomer = viewModel {
                ViewModelCustomer(CustomerRepository())
            }


            val authViewModel: ViewModelAuth = viewModel { ViewModelAuth(AuthRepository(), context) }

            LaunchedEffect(Unit) {
            }

            TFG_MovilTheme {
                NavigationDrawer(navController) {
                    AppNavigation(
                        navController = navController,
                        authState = authViewModel.authState.collectAsState().value,
                        authViewModel = authViewModel,
                        serviceViewModel = serviceViewModel,
                        customerViewModel = customerViewModel
                    )

                }
            }
        }
    }
}