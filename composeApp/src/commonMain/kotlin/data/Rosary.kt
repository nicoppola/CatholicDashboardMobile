package data

import kotlinx.serialization.Serializable

@Serializable
data class Rosary(
    val mysteries: List<String>,
    val mysteryTitle: String,
    val link: String
)