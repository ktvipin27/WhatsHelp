package com.whatshelp.ui.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whatshelp.R
import com.whatshelp.data.model.FeedbackType
import com.whatshelp.ui.theme.GreyLight
import com.whatshelp.ui.theme.WhatsHelpTheme

/**
 * Created by Vipin KT on 01/12/21
 */
@Composable
fun FeedbackScreen(viewModel: FeedbackViewModel = viewModel()) {
    val feedbackType by viewModel.feedbackType.observeAsState()
    val feedback by viewModel.feedback.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val enableSubmission by viewModel.enableSubmission.observeAsState(false)

    val radioOptions = FeedbackType.values().toList()
    val scrollState = rememberScrollState()
    WhatsHelpTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.feedback_disclaimer),
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Label(text = stringResource(R.string.label_feedback_type))
            Spacer(modifier = Modifier.height(10.dp))
            GroupedRadioButton(
                radioOptions,
                selectedOption = feedbackType
            ) { viewModel.setFeedbackType(it) }
            Spacer(modifier = Modifier.height(10.dp))
            Label(text = stringResource(R.string.hint_feedback))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = feedback,
                onValueChange = viewModel::setFeedback,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 120.dp)
                    .padding(5.dp),
                maxLines = 5,
                trailingIcon = {
                    if (feedback.isNotEmpty())
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "Clear Text",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.setFeedback("")
                                }
                        )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Label(text = stringResource(R.string.hint_feedback_email))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::setEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = { viewModel.submitFeedback() },
                enabled = enableSubmission,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.action_submit).uppercase())
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = GreyLight
    )
}

@Composable
fun GroupedRadioButton(
    radioOptions: List<FeedbackType>,
    selectedOption: FeedbackType?,
    onOptionSelected: (FeedbackType) -> Unit
) {

    Column {
        radioOptions.forEach { fbType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (fbType == selectedOption),
                        onClick = { onOptionSelected(fbType) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == fbType,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                )
                Text(
                    text = stringResource(fbType.stringRes),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFeedbackScreen() {

}