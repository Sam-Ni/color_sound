package com.example.colorsound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.R
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.IndexToColor
import com.example.colorsound.util.SoundInfoFactory


@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun SoundCard(
    soundInfo: Sound,
    onClickStartPlay: (String, Int) -> Unit
) {

    Card(
        onClick = { onClickStartPlay(soundInfo.url, soundInfo.id) },
        modifier = Modifier
            .padding(15.dp, top = 10.dp, bottom = 8.dp, end = 15.dp),
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
                        SoundInformation(createTime = soundInfo.createTime)
                    }
                }
            }
        }
    }
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
fun SoundInformation(createTime: String) {
    Text(
        text = createTime, style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(end = 10.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun SoundCardPreview() {
    ColorSoundTheme {
        SoundCard(soundInfo = SoundInfoFactory(), onClickStartPlay = { _, _ -> })
    }
}