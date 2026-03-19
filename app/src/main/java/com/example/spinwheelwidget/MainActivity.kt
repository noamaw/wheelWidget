package com.example.spinwheelwidget

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.spinwheel.logic.SpinWheelEngine
import com.example.spinwheel.models.WheelUiModel
import com.example.spinwheel.repository.ConfigRepository
import com.example.spinwheelwidget.ui.theme.SpinWheelWidgetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configRepository = ConfigRepository(this)
        setContent {
            var wheelUiModel by remember { mutableStateOf<WheelUiModel?>(null) }
            var rotation by remember { mutableFloatStateOf(0f) }
            SpinWheelWidgetTheme {
                LaunchedEffect(Unit) {
                    val config = configRepository.getConfig("https://raw.githubusercontent.com/noamaw/wheelWidget/refs/heads/main/urls_widget_config.json")
                    println(config)
                    wheelUiModel = configRepository.buildUiModel(config)
                    println(wheelUiModel)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text("Spin Wheel Coming Soon")
                    if (wheelUiModel != null ) {
                        wheelUiModel?.let {
                            Log.d("Widget", "Showing the widget")
                            ShowWheelUi(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowWheelUi(wheelUiModel: WheelUiModel) {
    var rotation by remember { mutableFloatStateOf(0f) }
    var isSpinning by remember { mutableStateOf(false) }

    val engine = remember { SpinWheelEngine() }

    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(
            durationMillis = wheelUiModel.rotationConfig.duration,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            isSpinning = false

            val selectedIndex = engine.getSelectedSegment(
                rotation = rotation,
                segmentCount = 12
            )

            Log.d("RESULT", "Selected index: $selectedIndex")
        },
        label = "rotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // Background
        Image(
            bitmap = wheelUiModel.background.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            // 🎡 Wheel (rotating)
            Image(
                bitmap = wheelUiModel.wheel.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer(rotationZ = animatedRotation)
            )

            // Frame
            Image(
                bitmap = wheelUiModel.frame.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )

            // Spin button
            Image(
                bitmap = wheelUiModel.spinButton.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        if (!isSpinning) {
                            isSpinning = true
                            rotation += engine.generateSpin(
                                wheelUiModel.rotationConfig.minimumSpins,
                                wheelUiModel.rotationConfig.maximumSpins
                            )
                        }
                    }
            )
        }
    }
}

