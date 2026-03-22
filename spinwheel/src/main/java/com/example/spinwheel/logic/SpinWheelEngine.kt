package com.example.spinwheel.logic

class SpinWheelEngine {

    fun generateSpin(
        minSpins: Int,
        maxSpins: Int
    ): Float {
        val spins = (minSpins..maxSpins).random()
        val extra = (0..360).random()
        return spins * 360f + extra
    }

    fun getSelectedSegment(
        rotation: Float,
        segmentCount: Int
    ): Int {
        val segmentSize = 360f / segmentCount

        val normalized = ((-rotation % 360) + 360) % 360

        val adjustedRotation = (normalized + (segmentSize / 2)) % 360

        return (adjustedRotation / segmentSize).toInt() % segmentCount
    }
}