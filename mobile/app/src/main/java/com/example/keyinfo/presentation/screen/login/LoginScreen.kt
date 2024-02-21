package com.example.keyinfo.presentation.screen.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.components.AdviceText
import com.example.keyinfo.presentation.screen.components.AppBar
import com.example.keyinfo.presentation.screen.components.LoadingItem
import com.example.keyinfo.presentation.screen.components.OutlinedTextFieldWithLabel
import com.example.keyinfo.presentation.screen.components.PasswordTextField
import com.example.keyinfo.ui.theme.BaseButtonColor
import com.example.keyinfo.ui.theme.Values.BasePadding
import com.example.keyinfo.ui.theme.Values.BigRound
import com.example.keyinfo.ui.theme.Values.ButtonHeight
import com.example.keyinfo.ui.theme.Values.MoreSpaceBetweenObjects
import com.example.keyinfo.ui.theme.Values.SpaceBetweenObjects


@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val loginState by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar {
            viewModel.processIntent(LoginIntent.GoBack)
        }

        Text(
            text = stringResource(R.string.login_to),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W700),
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(top = MoreSpaceBetweenObjects, bottom = SpaceBetweenObjects)
        )

        OutlinedTextFieldWithLabel(
            label = stringResource(R.string.login),
            value = loginState.login,
            onValueChange = { viewModel.processIntent(LoginIntent.UpdateLogin(it)) },
            error = loginState.isErrorText,
            modifier = Modifier
        )

        PasswordTextField(
            label = stringResource(R.string.password),
            value = loginState.password,
            onValueChange = { viewModel.processIntent(LoginIntent.UpdatePassword(it)) },
            transformationState = loginState.isPasswordHide,
            onButtonClick = { viewModel.processIntent(LoginIntent.UpdatePasswordVisibility) },
            errorText = loginState.isErrorText,
            modifier = Modifier.padding(top = SpaceBetweenObjects)
        )
        Spacer(modifier = Modifier.height(MoreSpaceBetweenObjects))
        Button(
            onClick = {
                viewModel.processIntent(LoginIntent.Login)
            },
            shape = RoundedCornerShape(BigRound),
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = !loginState.isLoading && viewModel.isLoginButtonAvailable(),
            colors = BaseButtonColor
        ) {
            Text(
                text = stringResource(R.string.login_button),
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W600)
            )
        }

        if (loginState.isLoading) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BasePadding)
            )
            LoadingItem()
        }

        AdviceText(
            baseText = stringResource(R.string.need_register),
            clickableText = stringResource(R.string.need_register_clickable),
            onClick = { viewModel.processIntent(LoginIntent.GoToRegistration) },
            modifier = Modifier.weight(1f)
        )
    }
}