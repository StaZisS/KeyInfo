package com.example.keyinfo.presentation.screen.keytransfer.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.keyinfo.R
import com.example.keyinfo.domain.model.keys.User
import com.example.keyinfo.presentation.screen.components.PairButtons
import com.example.keyinfo.presentation.screen.keytransfer.components.SmallKeyCard
import com.example.keyinfo.presentation.screen.schedule.components.SearchRow
import com.example.keyinfo.ui.theme.Values
import java.time.OffsetDateTime

@Composable
fun SelectPersonDialog(
    audience: String,
    building: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    searchText: MutableState<String>,
    users: List<User>
) {
    Dialog(
        onDismissRequest = {
            onCancelClick()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(715.dp)
                .padding(Values.BasePadding),
            shape = RoundedCornerShape(Values.DialogRound),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(Values.BasePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.key_tranfser_dialog),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                )
                Spacer(modifier = Modifier.height(Values.BasePadding))
                SmallKeyCard(
                    title = audience, description = building
                )
                Spacer(modifier = Modifier.height(Values.BasePadding))
                SearchRow(
                    searchText = searchText
                )
                val filteredUsers = filterUsers(users, searchText.value)
                LazyColumn(Modifier.height(340.dp)) {
                    if (filteredUsers.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(Values.BasePadding))
                            Text(
                                text = stringResource(id = R.string.not_found),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    } else {
                        items(filteredUsers.size) {
                            SmallKeyCard(
                                filteredUsers[it].name,
                                filteredUsers[it].roles.first(),
                                isAudience = false,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Values.BasePadding))
                PairButtons(
                    firstLabel = stringResource(id = R.string.confirm),
                    firstClick = { onConfirmClick() },
                    secondLabel = stringResource(id = R.string.cancel),
                    secondClick = { onCancelClick() },
                    modifier = Modifier
                )
            }
        }
    }
}

fun filterUsers(users: List<User>, searchText: String): List<User> {
    return users.filter { user ->
        user.name.contains(searchText, ignoreCase = true)
    }
}


@Preview
@Composable
fun PersonPreview() {
    val text = remember { mutableStateOf("") }
    val users = listOf(
        User("1", "Имя Фамилия", "mail@mail.ru", "Male", OffsetDateTime.now(), listOf("STUDENT"))
    )

    SelectPersonDialog(
        audience = "220",
        building = "2",
        onConfirmClick = { /*TODO*/ },
        onCancelClick = { /*TODO*/ },
        searchText = text,
        users = users
    )
}
