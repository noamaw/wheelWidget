package com.example.spinwheel.ui

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.spinwheel.local.WheelPrefs
import com.example.spinwheel.logic.SpinWheelEngine
import com.example.spinwheel.logic.updateWidget
import com.example.spinwheel.models.WheelUiModel
import com.example.spinwheel.repository.ConfigRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SpinWheelScreen(
    repository: ConfigRepository,
    prefs: WheelPrefs
) {
    var uiModel by remember { mutableStateOf<WheelUiModel?>(null) }

    LaunchedEffect(Unit) {
        val config = repository.getConfig(
            "https://raw.githubusercontent.com/noamaw/wheelWidget/refs/heads/main/urls_widget_config.json"
        )
        uiModel = repository.buildUiModel(config)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        uiModel?.let {
            SpinWheelContent(
                wheelUiModel = it,
                prefs = prefs
            )
        }
    }
}

@Composable
fun SpinWheelContent(
    wheelUiModel: WheelUiModel,
    prefs: WheelPrefs
) {
    val context = LocalContext.current

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

            val result = engine.getSelectedSegment(rotation, 12)

            // ✅ Save result
            prefs.saveResult(result)

            // ✅ Update widget
            CoroutineScope(Dispatchers.IO).launch {
                updateWidget(context)
            }

            Log.d("RESULT", "Selected index: $result")
        },
        label = "rotation"
    )

    val hasStarted = remember { mutableStateOf(false) }

    LaunchedEffect(wheelUiModel) {
        if (!hasStarted.value) {
            hasStarted.value = true
            isSpinning = true
            rotation += engine.generateSpin(
                wheelUiModel.rotationConfig.minimumSpins,
                wheelUiModel.rotationConfig.maximumSpins
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

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

            Image(
                bitmap = wheelUiModel.wheel.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer(rotationZ = animatedRotation)
            )

            Image(
                bitmap = wheelUiModel.frame.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )

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