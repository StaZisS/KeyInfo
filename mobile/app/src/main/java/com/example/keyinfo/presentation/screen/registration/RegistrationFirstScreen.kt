package com.example.keyinfo.presentation.screen.registration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.keyinfo.domain.validator.EmailValidator
import com.example.keyinfo.domain.validator.NameValidator
import com.example.keyinfo.presentation.screen.components.AdviceText
import com.example.keyinfo.presentation.screen.components.AppBar
import com.example.keyinfo.presentation.screen.components.DatePickerField
import com.example.keyinfo.presentation.screen.components.OutlinedTextFieldWithLabel
import com.example.keyinfo.presentation.screen.components.RoleSelectionButton
import com.example.keyinfo.ui.theme.BaseButtonColor
import com.example.keyinfo.ui.theme.Values.BasePadding
import com.example.keyinfo.ui.theme.Values.BigRound
import com.example.keyinfo.ui.theme.Values.MoreSpaceBetweenObjects
import com.example.keyinfo.ui.theme.Values.SpaceBetweenObjects

@Composable
fun RegistrationFirstScreen(
    viewModel: RegistrationViewModel
) {
    val focusManager = LocalFocusManager.current
    val registrationState by viewModel.state.collectAsState()

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
            viewModel.processIntent(RegistrationIntent.GoBackToAuth)
        }

        Text(
            text = stringResource(R.string.registration),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W700
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = MoreSpaceBetweenObjects,
                bottom = SpaceBetweenObjects
            )
        )

        LazyColumn {
            item {
                OutlinedTextFieldWithLabel(
                    label = stringResource(R.string.name),
                    value = registrationState.name,
                    onValueChange = {
                        viewModel.processIntent(RegistrationIntent.UpdateName(it))
                        viewModel.processIntent(
                            RegistrationIntent.UpdateErrorText(
                                NameValidator(),
                                it
                            )
                        )
                    },
                    error = registrationState.isErrorNameText,
                    modifier = Modifier
                )

                OutlinedTextFieldWithLabel(
                    label = stringResource(R.string.email),
                    value = registrationState.email,
                    onValueChange = {
                        viewModel.processIntent(RegistrationIntent.UpdateEmail(it))
                        viewModel.processIntent(
                            RegistrationIntent.UpdateErrorText(
                                EmailValidator(),
                                it
                            )
                        )

                    },
                    error = registrationState.isErrorEmailText,
                    modifier = Modifier.padding(top = SpaceBetweenObjects)
                )

                DatePickerField(
                    viewModel = viewModel,
                    state = registrationState
                )

                Button(
                    onClick = { viewModel.processIntent(RegistrationIntent.GoToSecondScreen) },
                    shape = RoundedCornerShape(BigRound),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MoreSpaceBetweenObjects)
                        .height(IntrinsicSize.Min),
                    enabled = viewModel.isContinueButtonAvailable(),
                    colors = BaseButtonColor
                ) {
                    Text(
                        text = stringResource(R.string.continue_),
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W600)
                    )
                }
            }
        }

        AdviceText(
            baseText = stringResource(R.string.need_login),
            clickableText = stringResource(R.string.need_login_clickable),
            onClick = { viewModel.processIntent(RegistrationIntent.GoToLogin) },
            modifier = Modifier.weight(1f)
        )
    }
}
