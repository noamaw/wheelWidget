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

        // 1. Normalize rotation to 0..359
        // We negate rotation because usually, a 'positive' spin
        // moves the wheel clockwise, meaning the 'pointer' moves counter-clockwise relative to the wheel.
        val normalized = ((-rotation % 360) + 360) % 360

        // 2. Adjust for the starting "Middle of Segment" position.
        // If the arrow points to the middle of segment 0 at rotation 0,
        // we add half a segment size to the calculation.
        val adjustedRotation = (normalized + (segmentSize / 2)) % 360

        // 3. Calculate index
        return (adjustedRotation / segmentSize).toInt() % segmentCount
    }
}