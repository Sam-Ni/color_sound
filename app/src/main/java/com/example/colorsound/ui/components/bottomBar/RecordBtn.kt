package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.colorsound.ui.screens.home.RecordState

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
}

data class RecordBtnVM(
    val onClick: () -> Unit,
    val onLongClick: () -> Unit,
    val recordState: RecordState,
    val isGranted: Boolean,
    val askPermission: () -> Unit,
    val visible: Boolean,
)