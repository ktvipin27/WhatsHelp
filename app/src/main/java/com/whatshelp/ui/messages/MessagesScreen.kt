package com.whatshelp.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whatshelp.R
import com.whatshelp.data.db.entity.Message
import com.whatshelp.ui.theme.GreyInfo
import com.whatshelp.ui.theme.WhatsHelpTheme

/**
 * Created by Vipin KT on 06/12/21
 */
@ExperimentalMaterialApi
@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel = viewModel(),
    onClickMessageItem: (Message) -> Unit,
    onClickFab: () -> Unit
) {
    val listState = rememberLazyListState()
    val messages by viewModel.messages.observeAsState(emptyList())

    WhatsHelpTheme {
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFab,
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }) {
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
                            MessageItem(item, onClickMessageItem)
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
    }
}


@ExperimentalMaterialApi
@Composable
fun MessageItem(message: Message, onClickItem: (Message) -> Unit) {
    Surface(
        onClick = { onClickItem(message) }
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

@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewMessagesScreen() {
    WhatsHelpTheme {
        MessagesScreen(onClickMessageItem = {}, onClickFab = {})
    }
}