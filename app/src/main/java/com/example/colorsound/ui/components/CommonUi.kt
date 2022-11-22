package com.example.colorsound.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.ColorSoundDestination
import com.example.colorsound.Home
import com.example.colorsound.R
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.COLORS


@Composable
fun ColorSoundTapRow(
    allScreen: List<ColorSoundDestination>,
    onTabSelected: (ColorSoundDestination) -> Unit,
    currentScreen: ColorSoundDestination,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier.fillMaxWidth()
    ) {
        allScreen.forEach { screen ->
            BottomNavigationItem(
                selected = currentScreen == screen,
                onClick = { onTabSelected(screen) },
                label = { Text(screen.route) },
                icon = { Icon(imageVector = screen.icon, contentDescription = null) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    ColorSoundTheme {
        ColorSoundTapRow(colorSoundTabRowScreens, {}, Home)
    }
}

@Composable
fun SoundList(
    soundList: List<Sound>,
    modifier: Modifier = Modifier,
    onClickStartPlay: (String, Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(soundList) { item ->
            SoundCard(soundInfo = item, onClickStartPlay = onClickStartPlay)
        }
    }
}

@Composable
fun ColorCircle(
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        modifier = modifier
            .size(56.dp)
        ,
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundCard(
    modifier: Modifier = Modifier,
    soundInfo: Sound,
    onClickStartPlay: (String ,Int) -> Unit
) {
    Card(
        modifier = modifier.fillMaxSize(),
        onClick = {onClickStartPlay(soundInfo.url, soundInfo.id)}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorCircle(
                drawableRes = COLORS[soundInfo.color],
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                text = soundInfo.name,
                modifier = Modifier.weight(1f)
            )
            SoundInformation(
//            duration = soundInfo.duration,
                createTime = soundInfo.createTime,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun SoundInformation(
    modifier: Modifier = Modifier,
//    duration: String,
    createTime: String,
) {
    Column(
        modifier = modifier
    ) {
//        Text(text = duration)
        Text(text = createTime)
    }
}

@Preview(showBackground = true)
@Composable
fun SoundCardPreview() {
    ColorSoundTheme {
//        SoundList(listOf(
//            Sound(1, R.drawable.circle, "Sound name", "12", "1")
//        ),
////            onClickStartPlay = {}
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun SoundListPreview() {
    ColorSoundTheme {
//        SoundList(soundList = DataSource.soundList, onClickStartPlay = {})
    }
}