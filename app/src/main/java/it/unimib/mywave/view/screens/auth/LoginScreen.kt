package it.unimib.mywave.view.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.unimib.mywave.R
import it.unimib.mywave.graphs.AuthScreen
import it.unimib.mywave.data.AuthUIEvent
import it.unimib.mywave.graphs.Graph
import it.unimib.mywave.ui.theme.*
import it.unimib.mywave.view.components.*
import it.unimib.mywave.viewmodel.AuthViewModel


@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val isError = authViewModel.authUIState.value.loginError != null

    if(authViewModel.hasUser){
        navController.navigate(Graph.HOME)
    }else {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {
                    HeadingTextComponent(
                        value = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BoxWithConstraints {
                        if ((maxHeight > 110.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_wave),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(110.dp)
                                    .fillMaxWidth(),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .offset(y = (-30).dp)
                        .background(
                            color = Color.White,
                            RoundedCornerShape(
                                topStart = 30.dp,
                                topEnd = 30.dp
                            )
                        )
                        .padding(28.dp)
                        .verticalScroll(rememberScrollState())
                )
                {
                    Surface(
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            NormalTextComponent(value = stringResource(id = R.string.auth_hello))
                            HeadingTextComponent(
                                value = stringResource(id = R.string.auth_welcome),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            MyTextField(
                                labelValue = stringResource(id = R.string.auth_email),
                                labelIco = painterResource(id = R.drawable.ic_email),
                                onTextSelected = {
                                    authViewModel.onEvent(AuthUIEvent.LoginEmailChanged(it))
                                },
                                errorStatus = true
                            )
                            MyTextFieldPass(
                                labelValue = stringResource(id = R.string.auth_password),
                                labelIco = painterResource(id = R.drawable.ic_lock),
                                onTextSelected = {
                                    authViewModel.onEvent(AuthUIEvent.LoginPasswordChanged(it))
                                },
                                errorStatus = true,
                                imeAction = ImeAction.Done
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            UnderLinedTextComponent(
                                value = stringResource(id = R.string.auth_forgot_password),
                                onTextSelected = {
                                    navController.navigate(route = AuthScreen.RecoveryScreen)
                                })
                            Spacer(modifier = Modifier.height(20.dp))
                            if (isError) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = authViewModel.authUIState.value.loginError
                                        ?: "unknown error",
                                    color = Color.Red,
                                )
                            }
                            ButtonComponent(
                                value = stringResource(id = R.string.auth_login),
                                onButtonClicked = {
                                    authViewModel.onEvent(AuthUIEvent.LoginButtonClicked)
                                },
                                isEnabled = true
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            DividerComponent()
                            ClickableLoginTextComponent(tryLogin = false, onTextSelected = {
                                navController.navigate(route = AuthScreen.SignUpScreen)
                            })
                        }
                    }
                }
            }

            if (authViewModel.authInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}
