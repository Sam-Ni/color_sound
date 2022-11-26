package com.example.colorsound.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import com.example.colorsound.model.Sound

fun SoundInfoFactory(): Sound {
    return Sound(1, 1, "newAdd", "2022-11-26 17:03:52", "123", "1:32")
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ButtonWithLongPress(
    onClick: () -> Unit,
    onDoubleClick:()->Unit = {},
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        onClick = { },
        modifier = modifier
            .combinedClickable(
                interactionSource,
                rememberRipple(),
                true,
                null,
                Role.Button,
                null,
                onClick = { onClick() },
                onLongClick = { onLongClick() },
                onDoubleClick = {onDoubleClick()}),
        enabled = enabled,
        shape = shape,
//        color = colors.backgroundColor(enabled).value,
//        contentColor = contentColor.copy(alpha = 1f),
        border = border,
//        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource,
    ) {
            ProvideTextStyle(
//                value = MaterialTheme.typography.button,
                value = MaterialTheme.typography.labelMedium,
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding)
                        .combinedClickable(interactionSource,
                            null,
                            true,
                            null,
                            Role.Button,
                            null,
                            onClick = { onClick() },
                            onLongClick = { onLongClick() },
                            onDoubleClick = { onDoubleClick() }),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }