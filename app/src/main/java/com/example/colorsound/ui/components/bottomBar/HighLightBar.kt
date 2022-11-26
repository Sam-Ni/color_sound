package com.example.colorsound.ui.components.bottomBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun HighLightBar(
    onPush: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))

            PushButton(onPush = { /*TODO*/ })
            DeleteButton(onDelete = {})
            UpdateButton(onUpdate = { /*TODO*/ })
        }
    }
}

@Composable
fun DeleteButton(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onDelete) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
    }
}

@Composable
fun PushButton(
    onPush: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onPush) {
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
    }
}

@Composable
fun UpdateButton(
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onUpdate) {
        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun HighLightBarPreview() {
    ColorSoundTheme {
        HighLightBar({}, {}, {})
    }
}