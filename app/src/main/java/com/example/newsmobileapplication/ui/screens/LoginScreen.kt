package com.example.newsmobileapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.ui.theme.DarkerLightPurple
import com.example.newsmobileapplication.ui.theme.DarkerPastelPink
import com.example.newsmobileapplication.ui.theme.LightBlue
import com.example.newsmobileapplication.ui.theme.LightPurple
import com.example.newsmobileapplication.ui.theme.MediumBlue
import com.example.newsmobileapplication.ui.theme.PastelPink
import com.example.newsmobileapplication.ui.theme.PastelYellow
import com.example.newsmobileapplication.viewmodel.AuthViewModel
import com.example.newsmobileapplication.viewmodel.LoginState
import com.example.newsmobileapplication.viewmodel.LoginViewModel



@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel(), authViewModel: AuthViewModel = hiltViewModel()) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val showResetPasswordDialog = remember { mutableStateOf(false) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val loginState = viewModel.loginState.collectAsState()

    // Klavye kontrolcüsü
    val keyboardController = LocalSoftwareKeyboardController.current

    // Snackbar host state
    val snackbarHostState = remember { SnackbarHostState() }

    // Ekran boyutlarını alıyoruz
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels.toFloat()
    val screenHeight = displayMetrics.heightPixels.toFloat()

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            LightPurple,
            PastelPink,
            PastelYellow,
            PastelPink,
            LightBlue
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, screenHeight),
        tileMode = TileMode.Clamp
    )

    val radialOverlay = Brush.radialGradient(
        colors = listOf(
            Color(0xFFB8CEFF).copy(alpha = 0.7f),
            Color.Transparent
        ),
        center = Offset(screenWidth / 2, screenHeight / 8),
        radius = screenHeight / 5
    )

    val buttonBackgroundColor = Brush.linearGradient(
        colors = listOf(
            DarkerPastelPink,
            MediumBlue.copy(alpha = 0.6f),
            DarkerPastelPink
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(radialOverlay),
            contentAlignment = Alignment.BottomCenter
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = (Color.White.copy(0.5f))
                )
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 60.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Login",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkerLightPurple,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible.value) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            }
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // Klavyeyi kapat
                                //viewModel.signIn(email.value, password.value)
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            viewModel.signIn(email.value, password.value, onSuccess = {})
                        },
                        shape = RoundedCornerShape(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = textColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(buttonBackgroundColor, shape = RoundedCornerShape(45.dp))
                    ) {
                        Text(text = "Login", modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextButton(onClick = { navController.navigate("signup") }) {
                        Text(
                            text = "Don't have an account? Sign Up",
                            fontSize = 16.sp,
                            color = DarkerLightPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    TextButton(onClick = {
                        showResetPasswordDialog.value = true
                    }) {
                        Text(
                            text = "Forgot Password",
                            fontSize = 16.sp,
                            color = DarkerLightPurple
                        )
                    }
                }
            }

            // Dialog for email input
            if (showResetPasswordDialog.value) {
                AlertDialog(
                    onDismissRequest = { showResetPasswordDialog.value = false },
                    title = {
                        Text(
                            "Reset Password",
                            color = DarkerLightPurple, // Başlık rengi
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column {
                            Text(
                                "Please enter your email to receive password reset instructions.",
                                color = Color.DarkGray,
                                fontSize = 16.sp
                            )
                            OutlinedTextField(
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = { Text("Email", color = Color.Gray) }, // Label rengi
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                authViewModel.forgotPassword(email.value)
                                showResetPasswordDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkerLightPurple ,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Send")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showResetPasswordDialog.value = false }
                        ) {
                            Text(
                                "Cancel",
                                color = DarkerLightPurple // İptal butonunun metin rengi
                            )
                        }
                    },
                    containerColor = Color.White.copy(0.9f),
                    shape = RoundedCornerShape(25.dp)
                )


            }

            when (loginState.value) {
                is LoginState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LoginState.Success -> {
                    authViewModel.loadUserData()
                    navController.navigate("feed")
                }
                is LoginState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (loginState.value as LoginState.Error).error,
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    Log.d("SignInScreen", "Unknown state: ${loginState.value}")
                }
            }
        }
    }
}
