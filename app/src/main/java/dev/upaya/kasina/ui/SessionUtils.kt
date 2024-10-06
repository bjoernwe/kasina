package dev.upaya.kasina.ui

import dev.upaya.kasina.data.Session


internal fun List<Session?>.maxOnDuration(fallback: Float = 0f): Float {
    return this.maxOfOrNull { it?.onDuration  ?: fallback } ?: fallback
}


internal fun List<Session?>.maxOffDuration(fallback: Float = 0f): Float {
    return this.maxOfOrNull { it?.offDuration ?: fallback } ?: fallback
}


internal fun Session?.gradientTransitionPoint(): Float {
    if (this == null) return .5f
    val onDuration = this.onDuration
    val offDuration = this.offDuration
    val totalDuration = onDuration + offDuration
    return offDuration / totalDuration
}
