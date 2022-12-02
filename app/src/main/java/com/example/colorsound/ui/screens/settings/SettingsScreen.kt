package com.example.colorsound.ui.screens.settings

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.ui.vm.data.Language


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsScreenVM: SettingsScreenVM
) {

    settingsScreenVM.apply {
        val options = listOf(stringResource(R.string.chinese), stringResource(R.string.english))
        var selectedOptionText by remember {
            mutableStateOf(
                when (language) {
                    Language.Chinese -> options[0]
                    Language.English -> options[1]
                }
            )
        }
        var expanded by remember {
            mutableStateOf(false)
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = CenterVertically) {
                Text(text = stringResource(R.string.is_loop_play), style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isRepeatPlay, onCheckedChange = onIsRepeatedPlayChanged)
            }
            Row(verticalAlignment = CenterVertically) {
                Text(text = stringResource(R.string.language), style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.weight(1f))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    val context = LocalContext.current
                    TextField(
                        value = selectedOptionText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults
                            .TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedOptionText = option
                                    expanded = false
                                    when (option) {
                                        options[0] -> onLanguageSelect(Language.Chinese)
                                        options[1] -> onLanguageSelect(Language.English)
                                    }
                                    context.getActivity()?.recreate()
                                }
                            )
                        }
                    }
                }
            }
            Row(verticalAlignment = CenterVertically) {
                Text(text = stringResource(R.string.background_play), style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isBackPlay, onCheckedChange = onIsBackPlayChanged)
            }
        }
    }
}


data class SettingsScreenVM(
    val isRepeatPlay: Boolean,
    val onIsRepeatedPlayChanged: (Boolean) -> Unit,
    val onLanguageSelect: (Language) -> Unit,
    val language: Language,
    val isBackPlay: Boolean,
    val onIsBackPlayChanged: (Boolean) -> Unit,
)

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
