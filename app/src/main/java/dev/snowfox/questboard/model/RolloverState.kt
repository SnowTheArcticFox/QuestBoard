package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class RolloverState(

    val lastGeneration: Long = 0L

)