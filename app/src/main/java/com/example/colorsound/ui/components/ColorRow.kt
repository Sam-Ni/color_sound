package com.example.colorsound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.COLOR_NUMBER
import com.example.colorsound.util.indexToColor


@Composable
fun ColorChooseRow(
    colorChooseRowVM: ColorChooseRowVM,
    modifier: Modifier = Modifier,
) {
    colorChooseRowVM.apply {
        Row(
            modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 0 until COLOR_NUMBER) {
                IconButton(
                    onClick = {
                        if (currentColor == i && canCancelSelected) chooseColor(-1)
                        else chooseColor(i)
                    }, modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(indexToColor(i)),
                        modifier = Modifier.size(if (currentColor == i) 60.dp else 20.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ColorRowPreview() {
    ColorSoundTheme {
        ColorChooseRow(ColorChooseRowVM(currentColor = 0, chooseColor = {}))
    }
}

data class ColorChooseRowVM(
    val currentColor: Int, val chooseColor: (Int) -> Unit, val canCancelSelected: Boolean = false
)