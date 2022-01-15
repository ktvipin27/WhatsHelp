package com.whatshelp.ui.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hbb20.CountryCodePicker
import com.whatshelp.R
import com.whatshelp.ui.components.IconToggleButtons
import com.whatshelp.ui.theme.WhatsHelpTheme
import com.whatshelp.util.PhoneNumberVisualTransformation
import com.whatshelp.util.toDp
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by Vipin KT on 07/12/21
 */
@ExperimentalMaterialApi
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    onClickHistoryIcon: () -> Unit,
    onClickQuickMessageIcon: () -> Unit
) {
    val selectedIndex by viewModel.selectedAppIndex.collectAsState()
    val mobile by viewModel.mobile.collectAsState()
    val message by viewModel.message.collectAsState()
    val focusManager = LocalFocusManager.current

    WhatsHelpTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AndroidView(
                    modifier = Modifier
                        .height(48.dp)
                        .border(
                            1.dp,
                            Color.DarkGray.copy(alpha = 0.75f),
                            RoundedCornerShape(10.dp)
                        ),
                    factory = {
                        CountryCodePicker(it).apply {
                            setPadding(0, 4.toDp, 0, 4.toDp)
                            setDefaultCountryUsingNameCode(viewModel.countryNameCode.value)
                            resetToDefaultCountry()
                            setOnCountryChangeListener {
                                viewModel.countryCode.value = selectedCountryCode
                                viewModel.countryNameCode.value = selectedCountryNameCode
                            }
                        }
                    }
                )
                IconToggleButtons(
                    viewModel.supportedApps.map { it.icon },
                    selectedIndex
                ) { viewModel.setSelectedAppIndex(it) }
            }
            Spacer(modifier = Modifier.height(10.dp))
            NumberField(
                mobile = mobile,
                onValueChange = { m ->
                    viewModel.mobile.value = m
                },
                onClickTrailingIcon = onClickHistoryIcon,
                viewModel.countryNameCode
            )
            Spacer(modifier = Modifier.height(10.dp))
            MessageField(
                message = message,
                onValueChange = { m -> viewModel.message.value = m },
                onClickTrailingIcon = onClickQuickMessageIcon
            )
            Spacer(modifier = Modifier.height(16.dp))
            Actions(
                onClickShare = {
                    focusManager.clearFocus()
                    viewModel.onClickAction(ChatAction.SHARE_LINK)
                },
                onClickChat = {
                    focusManager.clearFocus()
                    viewModel.onClickAction(ChatAction.OPEN_CHAT)
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun NumberField(
    mobile: String,
    onValueChange: (String) -> Unit,
    onClickTrailingIcon: () -> Unit,
    countryCode: MutableStateFlow<String>
) {
    OutlinedTextField(
        value = mobile,
        onValueChange = {
            if (it.length <= 15 && it.isDigitsOnly()) {
                onValueChange(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = {
            Surface(onClick = onClickTrailingIcon) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_history),
                    contentDescription = ""
                )
            }
        },
        label = {
            Text(text = stringResource(id = R.string.hint_type_number))
        },
        singleLine = true,
        visualTransformation = PhoneNumberVisualTransformation(
            LocalContext.current,
            countryCode.value
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}

@ExperimentalMaterialApi
@Composable
private fun MessageField(
    message: String,
    onValueChange: (String) -> Unit,
    onClickTrailingIcon: () -> Unit
) {
    OutlinedTextField(
        label = {
            Text(
                text = stringResource(id = R.string.hint_type_message)
            )
        },
        value = message,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp),
        trailingIcon = {
            Surface(onClick = onClickTrailingIcon) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_quickreply_24),
                    contentDescription = ""
                )
            }
        },
        maxLines = 5
    )
}

@Composable
private fun Actions(onClickShare: () -> Unit, onClickChat: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onClickShare) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_share_24),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.action_share_link).uppercase())
        }
        Button(onClick = onClickChat) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_chat_24),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.action_send).uppercase())
        }
    }
}


@Preview
@Composable
fun PreviewChatScreen() {
    //ChatScreen()
}

@Preview
@Composable
fun PreviewActions() {
    WhatsHelpTheme() {
        Actions({}, {})
    }
}