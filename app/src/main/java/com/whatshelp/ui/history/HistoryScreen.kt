package com.whatshelp.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whatshelp.R
import com.whatshelp.data.db.entity.History
import com.whatshelp.ui.theme.GreyInfo
import com.whatshelp.ui.theme.WhatsHelpTheme

/**
 * Created by Vipin KT on 02/12/21
 */
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(),
    onClickHistoryItem: (History) -> Unit
) {
    val listState = rememberLazyListState()
    val history by viewModel.history.observeAsState(emptyList())

    WhatsHelpTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (history.isNotEmpty()) {
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
                    items(history) { item ->
                        HistoryItem(item, onClickHistoryItem)
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryItem(history: History, onClickItem: (History) -> Unit) {
    Surface(
        onClick = { onClickItem(history) }
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
                text = history.formattedNumber
            )
        }
    }
}