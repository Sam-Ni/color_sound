package com.example.colorsound.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
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
import com.example.colorsound.util.CustomIndication
import com.example.colorsound.util.IndexToColor
import com.example.colorsound.util.SoundInfoFactory


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun SoundCard(
    soundInfo: Sound,
    onPlayOrPause: (String, Int) -> Unit,
    onLongClick: (Sound) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(15.dp, top = 10.dp, bottom = 8.dp, end = 15.dp)
            .combinedClickable(
                onClick = { onPlayOrPause(soundInfo.url, soundInfo.id) },
                onLongClick = { onLongClick(soundInfo) },
                interactionSource = remember { MutableInteractionSource() },
                indication = CustomIndication(
                    pressColor = MaterialTheme.colorScheme.onSurface,
                    cornerRadius = CornerRadius(50f, 50f),
                    alpha = .3f
                )
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 12.dp, top = 20.dp, bottom = 20.dp, end = 12.dp)
            ) {
                ColorCircle(soundInfo.color)
                Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row() {
                        SoundName(name = soundInfo.name)
                    }
                    Row() {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Row() {
                        Spacer(modifier = Modifier.weight(1f))
                        SoundDuration(duration = soundInfo.duration)
                        SoundCreateTime(createTime = soundInfo.getDate())
                    }
                }
            }
        }
    }
}

private fun Sound.getDate(): String {
    val endIndex = this.createTime.indexOf(" ")
    return this.createTime.substring(0 until endIndex)
}

@Composable
fun ColorCircle(colorIndex: Int) {
    Image(
        painter = painterResource(R.drawable.circle),
        contentDescription = null,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
        colorFilter = ColorFilter.tint(IndexToColor(colorIndex)),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun SoundName(name: String) {
    Text(
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        text = name,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.width(220.dp)
    )
}

@Composable
fun SoundCreateTime(createTime: String) {
    Text(
        text = createTime, style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(end = 10.dp)
    )
}

@Composable
fun SoundDuration(duration: String) {
    Text(
        text = duration,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(end = 10.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun SoundCardPreview() {
    ColorSoundTheme {
        SoundCard(soundInfo = SoundInfoFactory(), onPlayOrPause = { _, _ -> }, onLongClick = {})
    }
}