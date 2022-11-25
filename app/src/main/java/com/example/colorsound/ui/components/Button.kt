package com.example.colorsound.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun ModifierButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun ModifierOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)

    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview
@Composable
fun ModifierButtonPreview() {
    ColorSoundTheme() {
        ModifierButton(text = "test", onClick = {})
    }
}

@Preview
@Composable
fun ModifierOutlinedButtonPreview() {
    ColorSoundTheme() {
        ModifierOutlinedButton(text = "test", onClick = {})
    }
}
