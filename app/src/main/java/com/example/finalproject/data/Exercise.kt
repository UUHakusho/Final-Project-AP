package com.example.finalproject.data

import android.os.CountDownTimer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Exercise(
    val key: String,
    val title: String,
    val description: String,
    var numSets: Int = 3,
    var numReps: Int = 9,
    var numWeight: Int = 45,
    var numRestMin: Int = 1,
    var numRestSec: Int = 30,
    var isComplete: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var countDown: CountDownTimer = object: CountDownTimer(0, 0){
        override fun onTick(millisUntilFinished: Long) {
            TODO("Not yet implemented")
        }
        override fun onFinish() {
            TODO("Not yet implemented")
        }
    }
) {
    override fun equals(other: Any?) : Boolean =
        if (other is Exercise) {
            key == other.key
        } else {
            false
        }
}

data class WorkoutHistory(
    var timestamp: LocalDateTime,
    var listExercises: List<Pair<String, Int>>
)
