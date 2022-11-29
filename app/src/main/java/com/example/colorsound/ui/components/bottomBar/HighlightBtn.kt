package com.example.colorsound.ui.components.bottomBar

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.ui.Home
import com.example.colorsound.ui.RouteDestination
import com.example.colorsound.ui.Setting
import com.example.colorsound.ui.World
import com.example.colorsound.ui.vm.data.PushState

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
                        val context = LocalContext.current
                        when (onPushResult) {
                            PushState.Idle -> {}
                            PushState.Success -> {
                                Snackbar() {
                                    Text(text = "Upload successfully!")
                                }
                                emitMessage(context, "Upload successfully!")
                                setUploadIdle()
                            }
                            PushState.Failure -> {
                                emitMessage(context, "Upload failed!\nTry again:)")
                                setUploadIdle()
                            }
                            PushState.Uploading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = "Uploading...",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
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
                    World, Setting -> {}
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
    val onPushResult: PushState,
    val setUploadIdle: () -> Unit,
)

private fun emitMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).apply {
        setGravity(Gravity.AXIS_SPECIFIED, 0, 550)

        show()
    }
}