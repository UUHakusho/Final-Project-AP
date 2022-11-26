package com.example.finalproject.data

data class Exercise(
    val key: String,
    val title: String,
    val description: String,
    var numSets: Int = 3,
    var numReps: Int = 9,
    var numWeight: Int = 45
) {
    override fun equals(other: Any?) : Boolean =
        if (other is Exercise) {
            key == other.key
        } else {
            false
        }
}
