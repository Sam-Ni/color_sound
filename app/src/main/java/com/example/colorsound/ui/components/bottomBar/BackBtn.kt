package com.example.colorsound.ui.components.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackBtn(
    backBtnVM: BackBtnVM
) {
    backBtnVM.apply {
        IconButton(onClick = exitHighlight) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
    }
}

data class BackBtnVM(
    val exitHighlight: () -> Unit,
)