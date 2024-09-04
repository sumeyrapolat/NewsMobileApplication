package com.example.newsmobileapplication.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.newsmobileapplication.viewmodel.SignUpState
import com.example.newsmobileapplication.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()){

    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    val signUpState = viewModel.signUpState.collectAsState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(LightPurple, PastelPink, PastelYellow, PastelPink, LightBlue),
        start = Offset(0f, 0f),
        end = Offset(0f, context.resources.displayMetrics.heightPixels.toFloat()),
        tileMode = TileMode.Clamp
    )

    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFB8CEFF).copy(alpha = 0.7f), Color.Transparent),
                        center = Offset(context.resources.displayMetrics.widthPixels.toFloat() / 2, context.resources.displayMetrics.heightPixels.toFloat() / 8),
                        radius = context.resources.displayMetrics.heightPixels.toFloat() / 5
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.5f))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 40.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Sign Up",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkerLightPurple
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = firstName.value,
                        onValueChange = { firstName.value = it },
                        label = { Text("First name", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(firstNameFocusRequester),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = lastName.value,
                        onValueChange = { lastName.value = it },
                        label = { Text("Last name", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(lastNameFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {  emailFocusRequester.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {passwordFocusRequester.requestFocus() }
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
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { confirmPasswordFocusRequester.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = confirmPassword.value,
                        onValueChange = { confirmPassword.value = it },
                        label = { Text("Confirm password", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(confirmPasswordFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible.value) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            }
                            IconButton(onClick = {
                                confirmPasswordVisible.value = !confirmPasswordVisible.value
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
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            if (password.value == confirmPassword.value) {
                                viewModel.signUp(email.value, password.value, firstName.value, lastName.value)
                            } else {
                                Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(  DarkerPastelPink,
                                        MediumBlue.copy(alpha = 0.6f),
                                        DarkerPastelPink),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                ),
                                shape = RoundedCornerShape(45.dp)
                            )
                    ) {
                        Text(text = "Sign Up", modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    TextButton(onClick = { navController.navigate("signin") }) {
                        Text(
                            text = "Already have an account? Login here!",
                            fontSize = 16.sp,
                            color = DarkerLightPurple
                        )
                    }
                }
            }
        }
    }

    when (signUpState.value) {
        is SignUpState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SignUpState.Success -> {
            Toast.makeText(context, (signUpState.value as SignUpState.Success).message, Toast.LENGTH_LONG).show()
            navController.navigate("signin") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
            viewModel.resetSignUpState() // Durumu sıfırlayın
        }
        is SignUpState.Error -> {
            Toast.makeText(context, (signUpState.value as SignUpState.Error).error, Toast.LENGTH_LONG).show()
            viewModel.resetSignUpState() // Durumu sıfırlayın
        }
        else -> {
            Log.d("SignUpScreen", "Unknown state: ${signUpState.value}")
        }
    }
}