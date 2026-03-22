package com.example.spinwheel.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import com.example.spinwheel.local.WheelPrefs
import com.example.spinwheel.logic.decodeSampledBitmap
import java.io.File

class SpinWheelWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val prefs = WheelPrefs(context)
        val result = prefs.getResult()
        val bg = decodeSampledBitmap(
            File(context.filesDir, "bg.png").path,
            400,
            400
        )
        val bgFile = File(context.filesDir, "bg.png")
        val bgBitmap = BitmapFactory.decodeFile(File(context.filesDir, "bg.png").path)
        val wheel = decodeSampledBitmap(
            File(context.filesDir, "wheel.png").path,
            300, 300
        )
        val wheelFile = File(context.filesDir, "wheel.png")
        val wheelBitmap = BitmapFactory.decodeFile(File(context.filesDir, "wheel.png").path)
        val frame = decodeSampledBitmap(
            File(context.filesDir, "frame.png").path,
            320, 320
        )
        val frameFile = File(context.filesDir, "frame.png")
        val frameBitmap = BitmapFactory.decodeFile(File(context.filesDir, "frame.png").path)
        val spin = decodeSampledBitmap(
            File(context.filesDir, "spin.png").path,
            100, 100
        )
        val spinFile = File(context.filesDir, "spin.png")
        val spinBitmap = BitmapFactory.decodeFile(File(context.filesDir, "spin.png").path)

        provideContent {

            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                // Background
                if (bgFile.exists()) {
                    Image(
                        provider = ImageProvider(bg),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = GlanceModifier.fillMaxSize()
                    )
                }

                // Wheel
                if (wheelFile.exists()) {
                    Image(
                        provider = ImageProvider(wheel),
                        contentDescription = null,
                        modifier = GlanceModifier.size(80.dp)
                    )
                }

                // Frame
                if (frameFile.exists()) {
                    Image(
                        provider = ImageProvider(frame),
                        contentDescription = null,
                        modifier = GlanceModifier.size(100.dp)
                    )
                }

                // Spin button
                if (spinFile.exists()) {
                    Image(
                        provider = ImageProvider(spin),
                        contentDescription = "Spin",
                        modifier = GlanceModifier
                            .size(20.dp)
                            .clickable(
                                actionStartActivity<SpinWheelActivity>()
                            )
                    )
                }
            }
        }
    }
}