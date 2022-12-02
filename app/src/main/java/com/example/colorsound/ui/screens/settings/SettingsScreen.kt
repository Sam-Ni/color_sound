package com.example.colorsound.ui.screens.settings

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorsound.R


@Composable
fun SettingsScreen(
    settingsScreenVM: SettingsScreenVM
) {
    val context = LocalContext.current
    settingsScreenVM.apply {
        Column(modifier = Modifier.padding(20.dp)) {
            SettingTitle(name = stringResource(id = R.string.setting_title))

            Spacer(modifier = Modifier.height(20.dp))

            SettingSwitch(
                name = stringResource(R.string.is_loop_play),
                checked = isRepeatPlay,
                onCheckedChange = onIsRepeatedPlayChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            SettingButton(
                name = stringResource(R.string.language),
                currentText = currentLanguageText,
                onClick = {
                    onClickLanguageButton()
                    context.getActivity()?.recreate()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SettingSwitch(
                name = stringResource(R.string.background_play),
                checked = isBackPlay,
                onCheckedChange = onIsBackPlayChanged
            )
        }
    }
}


@Composable
fun SettingTitle(name: String) {
    Text(text = name, fontSize = 50.sp, style = MaterialTheme.typography.headlineLarge)
}

@Composable
fun SettingButton(name: String, currentText: String, onClick: () -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)

        ) {

            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = currentText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingSelect(
    name: String,
    currentText: String,
    options: List<String>,
    onClickMenuItem: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current


    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        ExposedDropdownMenuBox(
            modifier = Modifier.width(150.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = currentText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults
                        .TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor(),
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            expanded = false
                            onClickMenuItem(option)
                            context.getActivity()?.recreate()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun SettingSwitch(name: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                uncheckedIconColor = MaterialTheme.colorScheme.onSurface,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                checkedBorderColor = MaterialTheme.colorScheme.onSurface,
                checkedIconColor = MaterialTheme.colorScheme.onSurface,
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}

data class SettingsScreenVM(
    val isRepeatPlay: Boolean,
    val onIsRepeatedPlayChanged: (Boolean) -> Unit,
    val onClickLanguageButton: () -> Unit,
    val currentLanguageText: String,
    val isBackPlay: Boolean,
    val onIsBackPlayChanged: (Boolean) -> Unit,
)

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
