package com.example.coinlog.auth

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlog.R
import com.example.coinlog.data.CoinLogRepository
import com.example.coinlog.data.ExpenseDao
import com.example.coinlog.data.PotDao
import com.example.coinlog.data.SummaryDao
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fireStore = Firebase.firestore


    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    private val _credential = MutableStateFlow<CredentialManager?>(null)
    private val credential = _credential.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun uploadData(
        expenseDao: ExpenseDao,
        potDao: PotDao,
        summaryDao: SummaryDao,
        firestore: FirebaseFirestore
    ) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            CoinLogRepository(expenseDao, potDao, summaryDao, firestore).uploadUserData(userId)
        }
    }

    fun downloadData(
        expenseDao: ExpenseDao,
        potDao: PotDao,
        summaryDao: SummaryDao,
        firestore: FirebaseFirestore
    ) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            CoinLogRepository(
                expenseDao,
                potDao,
                summaryDao,
                firestore
            ).checkAndLoadUserData(userId)
        }
    }

    fun credentialUpdate(make: Boolean, context: Context?) {
        if (make && context != null) {
            _credential.update {
                CredentialManager.create(context)
            }
        } else {
            viewModelScope.launch {
                credential.value?.clearCredentialState(
                    ClearCredentialStateRequest()
                )
            }
        }
    }

    fun logInWithGoogle(context: Context) {
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
            } catch (e: Exception) {
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