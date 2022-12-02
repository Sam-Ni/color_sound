package com.example.colorsound.ui.components.bottomBar

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                        Icon(
                            painter = painterResource(id = if (currentScreen == screen) screen.selectIconResId else screen.iconResId),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}

data class NavBtnVM(
    val visible: Boolean,
    val allScreen: List<RouteDestination>,
    val onTabSelected: (RouteDestination) -> Unit,
    val currentScreen: RouteDestination,
)
