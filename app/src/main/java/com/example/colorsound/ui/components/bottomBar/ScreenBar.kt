@file:OptIn(ExperimentalFoundationApi::class)

package com.example.colorsound.ui.components.bottomBar

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.ColorSoundDestination
import com.example.colorsound.Home
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.ui.screens.home.RecordState
import com.example.colorsound.ui.theme.ColorSoundTheme


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ScreenBar(
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

    val recordBtnVM = RecordBtnVM(
        onClick = onClick,
        onLongClick = onLongClick,
        recordState = recordState,
        isGranted = isGranted,
        askPermission = askPermission,
        visible = currentScreen == Home && !isHighlightMode
    )


    val highlightBtnVM = HighlightBtnVM(
        visible = isHighlightMode,
        onDelete = onDelete,
        onPush = { /*TODO*/ },
        onUpdate = {/*TODO*/ }
    )

    val navBtnVM = NavBtnVM(
        visible = recordState == RecordState.Normal && !isHighlightMode,
        allScreen = allScreen,
        onTabSelected = onTabSelected
    )


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
            NavBtn(navBtnVM)
            HighlightBtn(highlightBtnVM)
            Spacer(modifier = Modifier.weight(1f))
            RecordBtn(recordBtnVM)
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    ColorSoundTheme {
        ScreenBar(
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
