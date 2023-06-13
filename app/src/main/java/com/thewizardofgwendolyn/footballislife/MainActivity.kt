package com.thewizardofgwendolyn.footballislife

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thewizardofgwendolyn.footballislife.ui.theme.FootballIsLifeTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayApp() {
    val image = painterResource(R.drawable.football_is_life_app_background)
    val context = LocalContext.current
    val footballIsLife = MediaPlayer.create(context, R.raw.football_is_life)
    val fightForward = MediaPlayer.create(context, R.raw.fight_forward)
    val frownsUpsideDown = MediaPlayer.create(context, R.raw.frowns_upside_down)

    val imageOffset = remember { mutableStateOf(Offset.Zero) }
    val imageSize = remember { mutableStateOf(IntSize.Zero) }

    FootballIsLifeTheme {
        Surface {
            Box (modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = image,
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .onGloballyPositioned { imageSize.value = it.size }
                        .offset { imageOffset.value.round() }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                if (imageSize.value.width > 0 && imageSize.value.height > 0) {
                                    val proposedOffset = imageOffset.value + dragAmount
                                    imageOffset.value = Offset(
                                        x = proposedOffset.x.coerceIn(
                                            (imageSize.value.width - this.size.width).toFloat(),
                                            0f
                                        ),
                                        y = proposedOffset.y.coerceIn(
                                            (imageSize.value.height - this.size.height).toFloat(),
                                            0f
                                        )
                                    )
                                    change.consume()
                                }
                            }
                        }
                )
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = { footballIsLife.start() }
                    ) {
                        Text("Football is Life")
                    }
                    Button(onClick = { fightForward.start() }) {
                        Text("Fight Forward")
                    }
                    Button(onClick = { frownsUpsideDown.start() }) {
                        Text("Frowns Upside Down")
                    }
                }
            }
        }
    }
}
