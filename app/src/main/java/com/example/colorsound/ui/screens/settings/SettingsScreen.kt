package com.example.colorsound.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen(
    settingsScreenVM: SettingsScreenVM
) {
    settingsScreenVM.apply {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "IsRepeatPlay:", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isRepeatPlay, onCheckedChange = onIsRepeatedPlayChanged)
            }
        }
    }
}


data class SettingsScreenVM(
    val isRepeatPlay: Boolean, val onIsRepeatedPlayChanged: (Boolean) -> Unit
)