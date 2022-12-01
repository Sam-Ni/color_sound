package com.example.colorsound.ui.components.bottomBar

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.RouteDestination
import com.example.colorsound.ui.Home
import com.example.colorsound.ui.vm.data.PushState
import com.example.colorsound.ui.vm.data.RecordState


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ScreenBar(
    screenBarVM: ScreenBarVM
) {
    screenBarVM.apply {
        val recordBtnVM = RecordBtnVM(
            onClick = onClick,
            onLongClick = onLongClick,
            recordState = recordState,
            isGranted = isGranted,
            askPermission = askPermission,
            visible = currentScreen == Home && !isHighlightMode && !isPlaying
        )


        val highlightBtnVM = HighlightBtnVM(
            visible = isHighlightMode,
            onDelete = onDelete,
            onPush = onPush,
            onUpdate = onUpdate,
            onLoop = onLoop,
            currentScreen = currentScreen,
            onPushResult = onPushResult,
            setUploadIdle = setUploadIdle
//            exitHighlight = exitHighlight,
        )

        val navBtnVM = NavBtnVM(
            visible = recordState == RecordState.Normal && !isHighlightMode,
            allScreen = allScreen,
            onTabSelected = onTabSelected
        )

        val backBtnVM = BackBtnVM(
            exitHighlight = exitHighlight,
        )


        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
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
                if (isHighlightMode) BackBtn(backBtnVM) else RecordBtn(recordBtnVM)
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

data class ScreenBarVM(
    val allScreen: List<RouteDestination>,
    val onTabSelected: (RouteDestination) -> Unit,
    val currentScreen: RouteDestination,
    val onClick: () -> Unit,
    val onLongClick: () -> Unit,
    val recordState: RecordState,
    val isGranted: Boolean,
    val askPermission: () -> Unit,
    val isHighlightMode: Boolean = false,
    val onDelete: () -> Unit,
    val onPush: () -> Unit,
    val onUpdate: () -> Unit,
    val exitHighlight: () -> Unit,
    val isPlaying: Boolean = false,
    val onLoop: () -> Unit,
    val onPushResult: PushState,
    val setUploadIdle: () -> Unit,
    val attachSound: (Sound, ExoPlayer) -> Unit,
    val detachSound: (Sound) -> Unit,
)

