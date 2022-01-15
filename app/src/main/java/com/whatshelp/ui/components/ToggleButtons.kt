package com.whatshelp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.whatshelp.R
import com.whatshelp.ui.theme.WhatsHelpTheme

/**
 * Created by Vipin KT on 28/12/21
 */
@Composable
fun IconToggleButtons(
    icons: List<Int>,
    selectedIndex: Int,
    onSelectionChange: (index: Int) -> Unit
) {
    val cornerRadius = 5.dp
    Row {
        icons.forEachIndexed { index, _ ->
            OutlinedButton(
                onClick = { onSelectionChange(index) },
                modifier = when (index) {
                    0 -> {
                        if (selectedIndex == index) {
                            Modifier
                                .offset(0.dp, 0.dp)
                                .zIndex(1f)
                        } else {
                            Modifier
                                .offset(0.dp, 0.dp)
                                .zIndex(0f)
                        }
                    }
                    else -> {
                        val offset = -1 * index
                        if (selectedIndex == index) {
                            Modifier
                                .offset(offset.dp, 0.dp)
                                .zIndex(1f)
                        } else {
                            Modifier
                                .offset(offset.dp, 0.dp)
                                .zIndex(0f)
                        }
                    }
                }
                    .height(48.dp)
                    .width(48.dp),
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(
                    1.dp, if (selectedIndex == index) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.75f)
                    }
                ),
                shape = when (index) {
                    // left outer button
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    // right outer button
                    icons.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    // middle button
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = "",
                    tint = if (selectedIndex == index) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.9f)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewIconToggleButtons() {
    WhatsHelpTheme() {
        IconToggleButtons(
            listOf(R.drawable.ic_whatsapp, R.drawable.ic_whatsapp_business),
            0
        ) {}
    }
}