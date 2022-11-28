package com.example.finalproject.data

import java.time.LocalDateTime

// This is the model in MVVM
data class Data(val name: String, val rating: Boolean)
// This abstracts either stable storage (file or database)
//   or the network

class Repository {
    companion object {
        private var exercisesList = listOf (
            Exercise("benchPress", "Bench Press", "An upper body exercise to" +
                    "target the pectoral muscles and front deltoids"),
            Exercise("backSquat", "Back Squat", "A lower body exercise to target" +
                    "the posterior chain, hamstrings, quadriceps, and gluteus maximus muscles"),
            Exercise("overheadPress", "Overhead Press", "A shoulder exercise"),
            Exercise("barbellRow", "BarbellRow", "A posterior chain exercise"),
        )

        private var workoutHistoryList: MutableList<WorkoutHistory> = mutableListOf()

        fun fetchData(): List<Exercise> {
            //val savedExercises = Json.decodeFromString<Exercise>("")
            return exercisesList
        }

        fun saveLatestExerciseDefaults(listExercise: List<Exercise>) {
            //val jsonList = Json.encodeToString(listExercise)
            for(exercise in listExercise) {
                if(exercisesList.contains(exercise)) {
                    val ex = exercisesList[exercisesList.indexOf(exercise)]
                    ex.numSets = exercise.numSets
                    ex.numReps = exercise.numReps
                    ex.numWeight = exercise.numWeight
                    ex.numRestMin = exercise.numRestMin
                    ex.numRestSec = exercise.numRestSec
                }
            }
        }

        fun addWorkoutItem(listExercise: List<Exercise>) {
            workoutHistoryList.add(WorkoutHistory(LocalDateTime.now(), listExercise))
        }
    }
}