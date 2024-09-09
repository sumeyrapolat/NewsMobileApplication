package com.example.newsmobileapplication.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.newsmobileapplication.data.preferences.PreferencesManager
import com.example.newsmobileapplication.ui.theme.*
import com.example.newsmobileapplication.viewmodel.AuthViewModel
import com.example.newsmobileapplication.viewmodel.LoginState
import com.example.newsmobileapplication.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesManager: PreferencesManager = PreferencesManager(LocalContext.current)
) {

    val context = LocalContext.current
    val email = remember { mutableStateOf(preferencesManager.getEmail() ?: "") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val showResetPasswordDialog = remember { mutableStateOf(false) }
    val rememberMeChecked = remember { mutableStateOf(preferencesManager.isRememberMeChecked()) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    NavyBlue
    val loginState = viewModel.loginState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            NavyBlue,
            DarkBlue
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, context.resources.displayMetrics.heightPixels.toFloat()),
        tileMode = TileMode.Clamp
    )

    val radialOverlay = Brush.radialGradient(
        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent),
        center = Offset(context.resources.displayMetrics.widthPixels.toFloat() / 2, context.resources.displayMetrics.heightPixels.toFloat() / 5),
        radius = context.resources.displayMetrics.heightPixels.toFloat() / 3
    )

    val buttonBackgroundColor = Brush.linearGradient(
        colors = listOf(
            SoftBlue,
            GrayBlue,
            SoftBlue
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
                    containerColor = (Color.White)
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
                        color = NavyBlue,
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
                                keyboardController?.hide()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Remember Me Checkbox bileşenini daha estetik hale getiriyoruz
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 8.dp, top = 8.dp)
                    ) {
                        Checkbox(
                            checked = rememberMeChecked.value,
                            onCheckedChange = { rememberMeChecked.value = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor =  GrayBlue, // Seçili olduğunda checkbox rengi
                                uncheckedColor = Color.Gray, // Seçilmediğinde checkbox rengi
                                checkmarkColor = Color.White // Tik işaretinin rengi
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Checkbox ile text arasına boşluk ekliyoruz
                        Text(
                            text = "Remember Me",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color =  Color.Black,
                        )
                    }


                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            if (password.value.length < 8) {
                                Toast.makeText(context, "Password must be at least 8 characters!", Toast.LENGTH_SHORT).show()
                            } else {
                                if (rememberMeChecked.value) {
                                    preferencesManager.saveLoginData(email.value, true)
                                } else {
                                    preferencesManager.clearLoginData()
                                }
                                viewModel.login(email.value, password.value, onSuccess = {})
                            }
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
                            color =  NavyBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    TextButton(onClick = {
                        showResetPasswordDialog.value = true
                    }) {
                        Text(
                            text = "Forgot Password",
                            fontSize = 16.sp,
                            color =  NavyBlue
                        )
                    }
                }
            }

            if (showResetPasswordDialog.value) {
                AlertDialog(
                    onDismissRequest = { showResetPasswordDialog.value = false },
                    title = {
                        Text(
                            "Reset Password",
                            color =  NavyBlue,
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
                                label = { Text("Email", color = Color.Gray) },
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
                                containerColor = GrayBlue,
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
                                color =NavyBlue
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
