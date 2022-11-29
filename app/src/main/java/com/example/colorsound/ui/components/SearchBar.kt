package com.example.colorsound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchBarVM: SearchBarVM
) {
    searchBarVM.apply {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            BasicTextField(
                singleLine = true,
                value = text,
                onValueChange = { onValueChange(it) },
                textStyle = MaterialTheme.typography.headlineLarge.copy(MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                decorationBox = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .focusRequester(focusRequester)
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "",
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = hint, style = MaterialTheme.typography.labelMedium
                                )
                            }
                            it()
                        }
                        if (text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onDeleteBtnClick()
                                    focusManager.clearFocus()
                                }, modifier = Modifier.size(20.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .height(50.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

data class SearchBarVM(
    val hint: String = "Search..",
    val text: String,
    val onValueChange: (String) -> Unit,
    val onDeleteBtnClick: () -> Unit
)