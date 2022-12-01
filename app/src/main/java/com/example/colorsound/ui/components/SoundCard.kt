package com.example.colorsound.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorsound.R
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory
import com.example.colorsound.util.indexToBackColor
import com.example.colorsound.util.indexToColor

data class SoundCardVM(
    val soundInfo: Sound,
    val onClick: (Sound) -> Unit,
    val onLongClick: (Sound) -> Unit,
    val isHighlight: Boolean = false,
    val isPlaying: Boolean = false,
    val isPlayingPaused: Boolean = false,
    val isPreparing: Boolean,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundCard(
    soundCardVM: SoundCardVM,
    modifier: Modifier = Modifier,
) {
    soundCardVM.apply {
        val isSystemInDarkTheme = isSystemInDarkTheme()

        //高亮动画
        val transition = updateTransition(isHighlight, label = "2")
        val elevation by transition.animateDp(label = "2") {
            if (it) 20.dp
            else 6.dp
        }
        val backColor by transition.animateColor(label = "2") {
            if (it) indexToBackColor(soundInfo.color, isSystemInDarkTheme)
            else MaterialTheme.colorScheme.surface
        }
        val frontColor by transition.animateColor(label = "2") {
            if (it) indexToColor(soundInfo.color)
            else MaterialTheme.colorScheme.onSurface
        }

        //播放动画
        val transition2 = updateTransition(isPlaying, label = "1")
        val fontSize by transition2.animateFloat(
            label = "1",
            transitionSpec = {
                spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium)
            },
        ) {
            if (it) 32f
            else 24f
        }
        val nameWidth by transition2.animateDp(
            label = "1",
            transitionSpec = {
                spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium)
            },
        ) {
            if (it) 240.dp
            else 180.dp
        }

        //触摸动画 TODO
        var isTouched by remember { mutableStateOf(false) }
        val transition3 = updateTransition(isTouched, label = "2")
        val contentSize by transition3.animateFloat(
            label = "2",
            transitionSpec = { spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium) }) {
            if (it) 0.9f
            else 1f
        }

        Row(
            modifier = modifier.padding(
                start = 20.dp, top = 10.dp, bottom = 15.dp, end = 20.dp
            )
        ) {
            Card(
                modifier = modifier.scale(contentSize),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            ) {
                Row(
                    modifier = Modifier
                        .background(backColor)
                        .clip(RoundedCornerShape(20.dp))
                        .combinedClickable(
                            onClick = { onClick(soundInfo) },
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
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row {
                                SoundName(
                                    name = if (isPreparing) "正在准备中" else soundInfo.name,
                                    frontColor,
                                    fontSize = fontSize.sp,
                                    width = nameWidth
                                )
                            }
                            Row {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Row(
                                modifier = Modifier.animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            ) {
                                if (isPlaying) {
                                    Text(
                                        text = if (!isPlayingPaused) "正在播放..." else "暂停",
                                        color = frontColor
                                    )
                                    Spacer(modifier = Modifier.height(48.dp))
                                }
                            }
                            Row {
                                Spacer(modifier = Modifier.height(8.dp))
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
}

private fun Sound.getDate(): String {
    return this.createTime.substring(0..9)
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
private fun SoundName(name: String, color: Color, fontSize: TextUnit = 24.sp, width: Dp = 180.dp) {
    Text(
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        text = name,
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = fontSize),
        color = color,
        modifier = Modifier.width(width)
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

