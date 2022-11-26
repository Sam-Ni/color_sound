package com.example.colorsound.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory
import com.example.colorsound.util.indexToBackColor
import com.example.colorsound.util.indexToColor


@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun SoundCard(
    soundCardVM: SoundCardVM,
    modifier: Modifier = Modifier,
) {
    soundCardVM.apply {
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val transition = updateTransition(isHighlight, label = "")
        val elevation by transition.animateDp(label = "") {
            if (it) 20.dp
            else 6.dp
        }

        val backColor by transition.animateColor(label = "") {
            if (it) indexToBackColor(soundInfo.color, isSystemInDarkTheme)
            else MaterialTheme.colorScheme.surface
        }

        val frontColor by transition.animateColor(label = "") {
            if (it) indexToColor(soundInfo.color)
            else MaterialTheme.colorScheme.onSurface
        }

        Card(
            modifier = modifier.padding(
                start = 20.dp, top = 10.dp, bottom = 15.dp, end = 20.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        ) {
            Row(
                modifier = Modifier
                    .background(backColor)
                    .clip(RoundedCornerShape(20.dp))
                    .combinedClickable(
                        onClick = { onPlayOrPause(soundInfo.url, soundInfo.id) },
                        onLongClick = { onLongClick(soundInfo) },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            radius = 300.dp,
                            color = indexToBackColor(soundInfo.color, isSystemInDarkTheme),
                            bounded = true
                        )
                    ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                        start = 12.dp, top = 20.dp, bottom = 20.dp, end = 12.dp
                    )
                ) {
                    ColorCircle(soundInfo.color)
                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row {
                            SoundName(name = soundInfo.name, frontColor)
                        }
                        Row {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            SoundDuration(duration = soundInfo.duration, frontColor)
                            SoundCreateTime(createTime = soundInfo.getDate(), frontColor)
                        }
                    }
                }
            }
        }
    }
}

private fun Sound.getDate(): String {
    return this.createTime.substring(0 .. 9)
}

@Composable
private fun ColorCircle(colorIndex: Int) {
    Image(
        painter = painterResource(R.drawable.circle),
        contentDescription = null,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
        colorFilter = ColorFilter.tint(indexToColor(colorIndex)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun SoundName(name: String, color: Color) {
    Text(
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        text = name,
        style = MaterialTheme.typography.headlineLarge,
        color = color,
        modifier = Modifier.width(220.dp)
    )
}

@Composable
private fun SoundCreateTime(createTime: String, color: Color) {
    Text(
        text = createTime,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        modifier = Modifier.padding(end = 10.dp)
    )
}

@Composable
private fun SoundDuration(duration: String, color: Color) {
    Text(
        text = duration,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = color,
        modifier = Modifier.padding(end = 10.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun SoundCardPreview() {
    ColorSoundTheme {
        SoundCard(
            SoundCardVM(soundInfo = SoundInfoFactory(),
                onPlayOrPause = { _, _ -> },
                onLongClick = {})
        )
    }
}

data class SoundCardVM(
    val soundInfo: Sound,
    val onPlayOrPause: (String, Int) -> Unit,
    val onLongClick: (Sound) -> Unit,
    val isHighlight: Boolean = true
)