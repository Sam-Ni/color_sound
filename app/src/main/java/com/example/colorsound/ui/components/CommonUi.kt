package com.example.colorsound.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.ColorSoundDestination
import com.example.colorsound.Home
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun ColorSoundTapRow(
    allScreen: List<ColorSoundDestination>,
    onTabSelected: (ColorSoundDestination) -> Unit,
    currentScreen: ColorSoundDestination,
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            allScreen.forEach { screen ->
                IconButton(onClick = { onTabSelected(screen) }) {
                    Icon(imageVector = screen.icon, contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    ColorSoundTheme {
        ColorSoundTapRow(colorSoundTabRowScreens, {}, Home)
    }
}
