@file:OptIn(ExperimentalFoundationApi::class)

package com.example.colorsound.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.ColorSoundDestination
import com.example.colorsound.Home
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.ui.screens.home.RecordState
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.ButtonWithLongPress


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
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            allScreen.forEach { screen ->
                IconButton(onClick = { onTabSelected(screen) }) {
                    Icon(imageVector = screen.icon, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (currentScreen == Home) {
                ColorSoundFAB(
                    onClick = onClick,
                    onLongClick = onLongClick,
                    recordState = recordState,
                    isGranted = isGranted,
                    askPermission = askPermission,
                )
            }
        }
    }
}

@Composable
private fun ColorSoundFAB(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    recordState: RecordState,
    isGranted: Boolean,
    askPermission: () -> Unit,
) {
    val imageVector = when (recordState) {
            RecordState.Normal -> Icons.Filled.Add
            RecordState.Recording -> Icons.Filled.Star
            RecordState.Pausing -> Icons.Filled.ArrowBack
        }
//    ButtonWithLongPress(
//        onClick = if (isGranted) onClick else askPermission,
//        onLongClick = if (isGranted) onLongClick else askPermission,
//    ) {
//        when (recordState) {
//            RecordState.Normal -> Icon(Icons.Filled.Add, contentDescription = null)
//            RecordState.Recording -> Icon(Icons.Filled.Star, contentDescription = null)
//            RecordState.Pausing -> Icon(Icons.Filled.ArrowBack, contentDescription = null)
//        }
//    }
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier.combinedClickable(
            onClick = if (isGranted) onClick else askPermission,
            onLongClick = if (isGranted) onLongClick else askPermission,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    ColorSoundTheme {
        ColorSoundTapRow(colorSoundTabRowScreens, {}, Home, {}, {}, RecordState.Normal, false, {})
    }
}
