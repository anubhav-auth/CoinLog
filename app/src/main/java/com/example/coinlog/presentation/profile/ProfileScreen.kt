package com.example.coinlog.presentation.profile

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coinlog.R
import com.example.coinlog.auth.AuthState
import com.example.coinlog.auth.AuthViewModel
import com.example.coinlog.data.FinanceViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun String.firstName(): String {
    return this.split(" ")[0]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    financeViewmodel: FinanceViewmodel,
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {


    BackHandler(
        enabled = financeViewmodel.bottomSheetVisible,
        onBack = {
            scope.launch {
                scaffoldState.bottomSheetState
                    .hide().also {
                        financeViewmodel.bottomSheetVisible = false
                    }
            }
        }
    )

    val authstate = authViewModel.authState.collectAsState()

    LaunchedEffect(authstate.value) {
        when (authstate.value) {
            AuthState.Unauthenticated -> navController.navigate("login_page")
            else -> Unit
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        PersonalDetails(authViewModel = authViewModel)
        OptionsCard(
            items = listOf(
                OptionCardContent(
                    R.drawable.finance_account,
                    "Account",
                    onClick = {
                        financeViewmodel.uploadToCloud()
                    }
                ),
                OptionCardContent(
                    R.drawable.finance_settings,
                    "Settings",
                    onClick = {
                        Log.d("mytag", "downloading.....")
                        financeViewmodel.downloadFromCloud()
                    }
                ),
                OptionCardContent(
                    R.drawable.finance_settings,
                    "Bill Split",
                    onClick = {

                    }
                ),
                OptionCardContent(
                    R.drawable.finance_logout,
                    "Logout",
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand().also {
                                financeViewmodel.bottomSheetVisible = true
                            }
                        }
                    }
                ),
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmLogout(
    sheetScaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    authViewModel: AuthViewModel,
    financeViewmodel: FinanceViewmodel
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Logout?",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Are you sure you wanna logout?",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row {

            Button(
                onClick = {
                    scope.launch {
                        sheetScaffoldState.bottomSheetState.hide().also {
                            financeViewmodel.bottomSheetVisible = false
                        }
                    }
                },
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = "No",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    authViewModel.signOut()
                    authViewModel.credentialUpdate(false, null)
                    financeViewmodel.bottomSheetVisible = false
                },
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = "Yes",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@Composable
fun PersonalDetails(authViewModel: AuthViewModel) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(72.dp))
        AsyncImage(
            model = authViewModel.auth.currentUser?.photoUrl,
            contentDescription = "",
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${authViewModel.auth.currentUser?.displayName?.firstName()}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "${authViewModel.auth.currentUser?.email}",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun OptionsCard(items: List<OptionCardContent>) {
    Spacer(modifier = Modifier.height(48.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 9.dp)
            .fillMaxWidth(),
    ) {
        items.forEach {
            OptionsCardItem(item = it)
        }
    }
}

@Composable
fun OptionsCardItem(item: OptionCardContent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .clickable { item.onClick() }
            .padding(9.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Icon(
                painter = painterResource(
                    id = item.icon
                ),
                modifier = Modifier
                    .size(45.dp)
                    .padding(6.dp),
                contentDescription = item.title,
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class OptionCardContent(
    val icon: Int,
    val title: String,
    val onClick: () -> Unit
)
