@file:OptIn(ExperimentalFoundationApi::class)

package com.example.colorsound.ui.components.bottomBar

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.ColorSoundDestination
import com.example.colorsound.Home
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.ui.screens.home.RecordState
import com.example.colorsound.ui.theme.ColorSoundTheme


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ColorSoundTapRow(
    allScreen: List<ColorSoundDestination>,
    onTabSelected: (ColorSoundDestination) -> Unit,
    currentScreen: ColorSoundDestination,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    recordState: RecordState,
    isGranted: Boolean,
    askPermission: () -> Unit,
    isHighlightMode: Boolean = false,
    onDelete: () -> Unit,
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))

            NavBtn(
                visible = recordState == RecordState.Normal && !isHighlightMode,
                allScreen = allScreen,
                onTabSelected = onTabSelected
            )

            HighlightBtn(
                visible = isHighlightMode,
                onDelete = onDelete,
                onPush = { /*TODO*/ },
                onUpdate = {/*TODO*/ })

            Spacer(modifier = Modifier.weight(1f))

            RecordBtn(
                onClick = onClick,
                onLongClick = onLongClick,
                recordState = recordState,
                isGranted = isGranted,
                askPermission = askPermission,
                visible = currentScreen == Home && !isHighlightMode,
            )

            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
private fun RecordBtn(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    recordState: RecordState,
    isGranted: Boolean,
    askPermission: () -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + expandHorizontally(),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkHorizontally()
    ) {
        val imageVector = when (recordState) {
            RecordState.Normal -> Icons.Filled.Add
            RecordState.Recording -> Icons.Filled.Star
            RecordState.Pausing -> Icons.Filled.ArrowBack
        }
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .combinedClickable(
                    onClick = if (isGranted) onClick else askPermission,
                    onLongClick = if (isGranted) onLongClick else askPermission,
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp
                    )
                )
                .size(24.dp)
        )
    }
}


@Composable
private fun HighlightBtn(
    visible: Boolean,
    onDelete: () -> Unit,
    onPush: () -> Unit,
    onUpdate: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + expandHorizontally(),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkHorizontally()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))

            PushButton(onPush = { /*TODO*/ })
            DeleteButton(onDelete = onDelete)
            UpdateButton(onUpdate = { /*TODO*/ })
        }
    }

}

@Composable
private fun NavBtn(
    visible: Boolean,
    allScreen: List<ColorSoundDestination>,
    onTabSelected: (ColorSoundDestination) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + expandHorizontally(),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkHorizontally()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            allScreen.forEach { screen ->
                IconButton(
                    onClick = { onTabSelected(screen) },
                ) {
                    Icon(imageVector = screen.icon, contentDescription = null)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    ColorSoundTheme {
        ColorSoundTapRow(
            colorSoundTabRowScreens,
            {},
            Home,
            {},
            {},
            RecordState.Normal,
            false,
            {}, false, {})
    }
}
