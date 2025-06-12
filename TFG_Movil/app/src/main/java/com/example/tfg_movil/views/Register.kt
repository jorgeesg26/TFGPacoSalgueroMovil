package com.example.tfg_movil.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tfg_movil.R
import com.example.tfg_movil.model.authentication.classes.AuthState
import com.example.tfg_movil.model.navigation.RutasNavegacion
import com.example.tfg_movil.viewmodel.ViewModelAuth

@Composable
fun Register(viewModelAuth: ViewModelAuth, navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var nickname by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    val authState by viewModelAuth.authState.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        profilePhotoUri = uri
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated ->
                navController.navigate(RutasNavegacion.Main.route) {
                    popUpTo(RutasNavegacion.Login.route) { inclusive = true }
                }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                viewModelAuth.resetAuthState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.Register),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(id = R.string.Email)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.Password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Warning
                val description = if (passwordVisible) {
                    stringResource(id = R.string.ShowPassword)
                } else {
                    stringResource(id = R.string.HidePassword)
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text("Seleccionar foto de perfil")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (profilePhotoUri != null) {
                    viewModelAuth.signUp(
                        nickname,
                        email,
                        password,
                        confirmPassword,
                        profilePhotoUri!!,
                        context.contentResolver
                    )
                } else {
                    Toast.makeText(context, "Selecciona una foto de perfil", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = authState != AuthState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.CreateAccount))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate(RutasNavegacion.Login.route) }) {
            Text(text = stringResource(id = R.string.MessageToRegister))
        }
    }
}
