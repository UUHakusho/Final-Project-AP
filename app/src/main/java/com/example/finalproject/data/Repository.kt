package com.example.finalproject.data

import com.github.mikephil.charting.data.Entry
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Data(val name: String, val rating: Boolean)

class Repository {
    companion object {
        private var exercisesList = listOf (
            Exercise("benchPress", "Bench Press", "An upper body exercise to" +
                    "target the pectoral muscles and front deltoids"),
            Exercise("backSquat", "Back Squat", "A lower body exercise to target" +
                    "the posterior chain, hamstrings, quadriceps, and gluteus maximus muscles"),
            Exercise("overheadPress", "Overhead Press", "A shoulder exercise"),
            Exercise("barbellRow", "Barbell Row", "A posterior chain exercise"),
        )

        private var workoutHistoryList: MutableList<WorkoutHistory> = mutableListOf(
            WorkoutHistory(LocalDateTime.now().with{it.minus(10, ChronoUnit.DAYS)},
                listOf((Pair<String, Int>("benchPress", 45)))),
            WorkoutHistory(LocalDateTime.now().with{it.minus(8, ChronoUnit.DAYS)},
                listOf((Pair<String, Int>("benchPress", 65)))),
            WorkoutHistory(LocalDateTime.now().with{it.minus(5, ChronoUnit.DAYS)},
                listOf((Pair<String, Int>("benchPress", 55)))),
            WorkoutHistory(LocalDateTime.now().with{it.minus(2, ChronoUnit.DAYS)},
                listOf((Pair<String, Int>("benchPress", 75))))
        )

        fun fetchData(): List<Exercise> {
            // val savedExercises = Json.decodeFromString<Exercise>("")
            return exercisesList
        }

        fun saveLatestExerciseDefaults(listExercise: List<Exercise>) {
            // val jsonList = Json.encodeToString(listExercise)
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
            val listMaxes = listExercise.map {
                it.key to it.numWeight
            }
            workoutHistoryList.add(WorkoutHistory(LocalDateTime.now(), listMaxes))
        }

        fun getWorkoutItemHistory(currExerciseKey: String): ArrayList<Entry> {
            val entries = ArrayList<Entry>()
            for(woHist in workoutHistoryList) {
                for(ex in woHist.listExercises) {
                    if(currExerciseKey == ex.first) {
                        entries.add(Entry(woHist.timestamp.dayOfYear.toFloat(), ex.second.toFloat()))
                    }
                }
            }
            return entries
        }
    }
}