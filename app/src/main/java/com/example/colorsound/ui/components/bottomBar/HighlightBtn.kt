package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HighlightBtn(
    highlightBtnVM: HighlightBtnVM
) {
    highlightBtnVM.apply {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + expandHorizontally(),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkHorizontally()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(15.dp))

                IconButton(onClick = onPush) {
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
                IconButton(onClick = onUpdate) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
            }
        }
    }
}

data class HighlightBtnVM(
    val visible: Boolean,
    val onDelete: () -> Unit,
    val onPush: () -> Unit,
    val onUpdate: () -> Unit
)