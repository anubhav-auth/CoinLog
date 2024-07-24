package com.example.coinlog.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.coinlog.auth.AuthState
import com.example.coinlog.auth.AuthViewModel

@Composable
fun ProfileScreen(viewModel: AuthViewModel, navController: NavController) {
    val authstate = viewModel.authState.collectAsState()

    LaunchedEffect(authstate.value) {
        when(authstate.value){
            AuthState.Unauthenticated -> navController.navigate("login_page")
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "profile")
        Button(onClick = {
            viewModel.signOut()
            viewModel.credentialUpdate(false, null)
        }) {
            Text(text = "signOut")
        }
        //add bill split feature
    }
}