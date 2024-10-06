package dev.upaya.kasina.ui

import dev.upaya.kasina.data.Session


internal fun Session?.onDuration(): Float {
    return this?.onDuration ?: 0f
}


internal fun Session?.offDuration(): Float {
    return this?.offDuration ?: 0f
}


internal fun Session?.onWeight(): Float {
    return this.onDuration()
}


internal fun Session?.offWeight(): Float {
    return this.offDuration()
}


internal fun List<Session?>.maxOnDuration(): Float {
    return this.maxOfOrNull { it?.onDuration ?: 0f } ?: 0f
}


internal fun List<Session?>.maxOffDuration(): Float {
    return this.maxOfOrNull { it?.offDuration ?: 0f } ?: 0f
}


internal fun List<Session?>.onGapWeight(i: Int): Float {
    return maxOnDuration() - this[i].onDuration()
}


internal fun List<Session?>.offGapWeight(i: Int): Float {
    return maxOffDuration() - this[i].offDuration()
}


internal fun List<Session?>.totalOnOffWeight(i: Int): Float {
    return this[i].onWeight() + this[i].offWeight()
}


internal fun Session?.gradientTransitionPoint(): Float {
    if (this == null) return .5f
    val onDuration = this.onDuration
    val offDuration = this.offDuration
    val totalDuration = onDuration + offDuration
    return offDuration / totalDuration
}
