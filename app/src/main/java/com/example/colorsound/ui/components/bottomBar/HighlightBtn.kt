package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.ui.Home
import com.example.colorsound.ui.RouteDestination

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

                when (currentScreen) {
                    is Home -> {
                        IconButton(onClick = onPush) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_upward_30px),
                                contentDescription = "",
                            )
                        }
                        IconButton(onClick = onDelete) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                        }
                        IconButton(onClick = onUpdate) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                        }

                    }
                    else -> {}
                }
                IconButton(onClick = onLoop) {
                    Icon(painter = painterResource(id = R.drawable.cached_30px), contentDescription = null)
                }
            }
        }
    }
}

data class HighlightBtnVM(
    val visible: Boolean,
    val onDelete: () -> Unit,
    val onPush: () -> Unit,
    val onUpdate: () -> Unit,
//    val exitHighlight: () -> Unit,
    val onLoop: () -> Unit,
    val currentScreen: RouteDestination,
)