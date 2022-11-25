package com.example.finalproject.data

data class Exercise(
    val key: String,
    val title: String,
    val description: String,
    val numSets: Int = 3,
    val numReps: Int = 10,
    val numWeight: Int = 45
) {
    override fun equals(other: Any?) : Boolean =
        if (other is Exercise) {
            key == other.key
        } else {
            false
        }
}
