package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.colorsound.ui.RouteDestination

@Composable
fun NavBtn(
    navBtnVM: NavBtnVM
) {
    navBtnVM.apply {
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
}

data class NavBtnVM(
    val visible: Boolean,
    val allScreen: List<RouteDestination>,
    val onTabSelected: (RouteDestination) -> Unit
)
