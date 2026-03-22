package com.example.spinwheel

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.spinwheel.ui.SpinWheelWidget

class SpinWheelWidgetReceiver : GlanceAppWidgetReceiver() {
    // Tell the receiver which widget UI to use
    override val glanceAppWidget: GlanceAppWidget = SpinWheelWidget()
}