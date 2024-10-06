package dev.upaya.kasina.ui

import dev.upaya.kasina.data.Session


internal fun List<Session?>.maxOnDuration(minDuration: Float = 1f): Float {
    return this.maxOfOrNull { it?.onDuration  ?: minDuration } ?: minDuration
}


internal fun List<Session?>.maxOffDuration(minDuration: Float = 1f): Float {
    return this.maxOfOrNull { it?.offDuration ?: minDuration } ?: minDuration
}


internal fun Session?.gradientTransitionPoint(): Float {
    if (this == null) return .5f
    val onDuration = this.onDuration.coerceAtLeast(1f)
    val offDuration = this.offDuration.coerceAtLeast(1f)
    val totalDuration = onDuration + offDuration
    return offDuration / totalDuration
}
