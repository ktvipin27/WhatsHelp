package com.whatshelp.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whatshelp.R
import com.whatshelp.data.db.entity.Message
import com.whatshelp.ui.theme.GreyInfo
import com.whatshelp.ui.theme.WhatsHelpTheme
import kotlinx.coroutines.launch

/**
 * Created by Vipin KT on 06/12/21
 */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel = viewModel(),
    onClickMessage: (Message) -> Unit,
    onClickFab: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val messages by viewModel.messages.observeAsState(emptyList())

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    val closeSheet: () -> Unit = {
        scope.launch {
            focusManager.clearFocus()
            modalBottomSheetState.hide()
        }
    }

    WhatsHelpTheme {
        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = {
                AddMessageSheet(
                    onClickSave = { message ->
                        viewModel.addMessage(message)
                        closeSheet()
                        scope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                    },
                    onClickCancel = closeSheet
                )
            }
        ) {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                        onClickFab()
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }) {
                MessagesContent(messages, listState, onClickMessage)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun MessagesContent(
    messages: List<Message>,
    listState: LazyListState,
    onClickMessage: (Message) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (messages.isNotEmpty()) {
            Text(
                text = stringResource(R.string.info_tap_to_select),
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreyInfo)
                    .padding(10.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = listState
            ) {
                items(messages) { item ->
                    MessageItem(item, onClickMessage)
                    Divider(color = GreyInfo, thickness = (0.5).dp)
                }
            }

        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.message_no_data),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MessageItem(message: Message, onClickMessage: (Message) -> Unit) {
    Surface(
        onClick = { onClickMessage(message) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = message.text
            )
        }
    }
}


@Composable
fun AddMessageSheet(onClickSave: (String) -> Unit, onClickCancel: () -> Unit) {
    var feedback by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    fun reset() {
        feedback = ""
        isError = false
    }

    fun validate() {
        isError = feedback.isEmpty() || feedback.isBlank()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.title_add_message),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.hint_type_message)
                )
            },
            value = feedback,
            isError = isError,
            onValueChange = {
                feedback = it
                if (isError)
                    validate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp)
                .padding(5.dp),
            maxLines = 5
        )
        if (isError) {
            Text(
                text = stringResource(id = R.string.error_no_message),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                onClickCancel()
                reset()
            }) {
                Text(text = stringResource(id = R.string.action_cancel).uppercase())
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                validate()
                if (!isError) {
                    onClickSave(feedback)
                    reset()
                }
            }) {
                Text(text = stringResource(id = R.string.action_save).uppercase())
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewMessagesScreen() {
    WhatsHelpTheme {
        MessagesScreen(onClickMessage = {}, onClickFab = {})
    }
}

@Preview
@Composable
fun PreviewAddMessageSheet() {
    WhatsHelpTheme {
        AddMessageSheet({}, {})
    }
}