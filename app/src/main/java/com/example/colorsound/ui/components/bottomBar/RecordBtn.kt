package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.colorsound.ui.vm.data.RecordState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordBtn(
    recordBtnVM: RecordBtnVM
) {
    recordBtnVM.apply {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + expandHorizontally(),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkHorizontally()
        ) {
            val imageId = when (recordState) {
                RecordState.Normal -> com.example.colorsound.R.drawable.record_start
                RecordState.Recording -> com.example.colorsound.R.drawable.record_pause
                RecordState.Pausing -> com.example.colorsound.R.drawable.record_continue
            }
            Icon(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .combinedClickable(
                        onClick = if (isGranted) {
                            {
                                stopCurrentSound()
                                onClick()
                            }
                        } else askPermission,
                        onLongClick = if (isGranted) onLongClick else askPermission,
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 20.dp
                        )
                    )
                    .size(30.dp)
            )
        }
    }
}

data class RecordBtnVM(
    val onClick: () -> Unit,
    val onLongClick: () -> Unit,
    val recordState: RecordState,
    val isGranted: Boolean,
    val askPermission: () -> Unit,
    val visible: Boolean,
    val stopCurrentSound: () -> Unit,
)