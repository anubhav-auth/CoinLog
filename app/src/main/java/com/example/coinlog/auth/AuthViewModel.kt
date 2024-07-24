package com.example.coinlog.auth

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlog.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    private val _credential = MutableStateFlow<CredentialManager?>(null)
    val credential = _credential.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun credentialUpdate(make: Boolean, context: Context?) {
        if (make && context != null) {
            _credential.update {
                CredentialManager.create(context)
            }
        }else{
            viewModelScope.launch {
                credential.value?.clearCredentialState(
                    ClearCredentialStateRequest()
                )
            }
        }
    }
    fun logInWithGoogle(context: Context){
        credentialUpdate(true, context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(context, R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credential.value?.getCredential(
                    context = context,
                    request = request
                )
                val credential = result?.credential
                val googleIdTokenCredential = credential?.data?.let {
                    GoogleIdTokenCredential
                        .createFrom(it)
                }
                val googleIDToken = googleIdTokenCredential?.idToken

                val fireBaseCredential = GoogleAuthProvider.getCredential(googleIDToken, null)

                auth.signInWithCredential(fireBaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _authState.update {
                                AuthState.Authenticated
                            }
                        } else {
                            _authState.update {
                                AuthState.Error(task.exception?.message ?: "Something went Wrong")
                            }
                        }
                    }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.update {
                AuthState.Unauthenticated
            }
        } else {
            _authState.update {
                AuthState.Authenticated
            }
        }
    }

    fun loginViaEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.update {
                AuthState.Error("Email os Password is empty")
            }
            return
        }
        _authState.update {
            AuthState.Loading
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.update {
                        AuthState.Authenticated
                    }
                } else {
                    _authState.update {
                        AuthState.Error(task.exception?.message ?: "Something went Wrong")
                    }
                }
            }
    }


    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.update {
                AuthState.Error("Email os Password is empty")
            }
            return
        }
        _authState.update {
            AuthState.Loading
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.update {
                        AuthState.Authenticated
                    }
                } else {
                    _authState.update {
                        AuthState.Error(task.exception?.message ?: "Something went Wrong")
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authState.update {
            AuthState.Unauthenticated
        }
    }

}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}