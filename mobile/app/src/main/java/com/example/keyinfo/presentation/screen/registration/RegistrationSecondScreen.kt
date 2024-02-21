package com.example.keyinfo.presentation.screen.registration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import com.example.keyinfo.domain.validator.ConfirmPasswordValidator
import com.example.keyinfo.domain.validator.PasswordValidator
import com.example.keyinfo.presentation.screen.components.AdviceText
import com.example.keyinfo.presentation.screen.components.AppBar
import com.example.keyinfo.presentation.screen.components.LoadingItem
import com.example.keyinfo.presentation.screen.components.PasswordTextField
import com.example.keyinfo.ui.theme.BaseButtonColor
import com.example.keyinfo.ui.theme.Values.BasePadding
import com.example.keyinfo.ui.theme.Values.BigRound
import com.example.keyinfo.ui.theme.Values.MoreSpaceBetweenObjects
import com.example.keyinfo.ui.theme.Values.SpaceBetweenObjects

@Composable
fun RegistrationSecondScreen (
    viewModel: RegistrationViewModel
) {
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsState()

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
            viewModel.processIntent(RegistrationIntent.GoBackToFirst)
        }

        Text(
            text = stringResource(R.string.registration),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W700),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = MoreSpaceBetweenObjects,
                bottom = SpaceBetweenObjects
            )
        )

        PasswordTextField(
            label = stringResource(R.string.password),
            value = state.password,
            onValueChange = {
                viewModel.processIntent(RegistrationIntent.UpdatePassword(it))
                viewModel.processIntent(
                    RegistrationIntent.UpdateErrorText(
                        PasswordValidator(),
                        it
                    )
                )
            },
            transformationState = state.isPasswordHide,
            onButtonClick = {viewModel.processIntent(RegistrationIntent.UpdatePasswordVisibility)},
            errorText = state.isErrorPasswordText,
            modifier = Modifier
        )


        PasswordTextField(
            label = stringResource(R.string.confirm_password),
            value = state.confirmPassword,
            onValueChange = {
                viewModel.processIntent(RegistrationIntent.UpdateConfirmPassword(it))
                viewModel.processIntent(
                    RegistrationIntent.UpdateErrorText(
                        ConfirmPasswordValidator(),
                        state.password,
                        it
                    )
                )
            },
            transformationState = state.isConfirmPasswordHide,
            onButtonClick = {
                viewModel.processIntent(RegistrationIntent.UpdateConfirmPasswordVisibility)
            },
            errorText = state.isErrorConfirmPasswordText,
            modifier = Modifier.padding(top = SpaceBetweenObjects)
        )

        Button(
            onClick = {
                viewModel.processIntent(RegistrationIntent.Registration(state))
            },
            shape = RoundedCornerShape(BigRound),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = BasePadding,
                    bottom = BasePadding
                )
                .height(IntrinsicSize.Min),
            enabled = viewModel.isRegisterButtonAvailable(),
            colors = BaseButtonColor
        ) {
            Text(
                text = stringResource(R.string.to_register)
            )
        }

        if (state.isLoading){
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BasePadding)
            )
            LoadingItem()
        }

        AdviceText(
            baseText = stringResource(R.string.need_login),
            clickableText = stringResource(R.string.need_login_clickable),
            onClick = { viewModel.processIntent(RegistrationIntent.GoToLogin) },
            modifier = Modifier.weight(1f)
        )
    }
}