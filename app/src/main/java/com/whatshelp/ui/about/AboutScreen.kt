package com.whatshelp.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whatshelp.BuildConfig
import com.whatshelp.R
import com.whatshelp.ui.theme.WhatsHelpTheme
import com.whatshelp.util.Constants
import java.util.*

/**
 * Created by Vipin KT on 01/12/21
 */

@Composable
fun AboutScreen(onClickProfile: (String) -> Unit) {

    WhatsHelpTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = .96f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(
                        R.string.app_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption
                )
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null
                )
                CopyRight(onClickProfile)
            }
            Text(
                text = stringResource(R.string.app_disclaimer),
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CopyRight(
    onClickProfile: (String) -> Unit
) {

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val string = stringResource(R.string.app_copyright, currentYear, currentYear + 1)
    val annotatedString = buildAnnotatedString {
        append(string)
        addStringAnnotation(
            tag = "profile",
            annotation = Constants.URL_DEVELOPER_PROFILE,
            start = string.length - 8, end = string.length
        )
        addStyle(
            style = SpanStyle(color = MaterialTheme.colors.primary),
            start = string.length - 8,
            end = string.length
        )
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.caption,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "profile",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClickProfile(it.item)
            }

        }
    )
}

@Preview
@Composable
fun PreviewScreen() {
    AboutScreen {

    }
}

@Preview
@Composable
fun PreviewCopyRight() {
    WhatsHelpTheme {
        CopyRight(onClickProfile = {})
    }
}