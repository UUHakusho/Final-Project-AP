package com.example.finalproject.data

// This is the model in MVVM
data class Data(val name: String, val rating: Boolean)
// This abstracts either stable storage (file or database)
//   or the network
class Repository {
    companion object {
        private var initialDataList = listOf (
            Exercise("benchPress", "Bench Press", "An upper body exercise to" +
                    "target the pectoral muscles and front deltoids"),
            Exercise("backSquat", "Back Squat", "A lower body exercise to target" +
                    "the posterior chain, hamstrings, quadriceps, and gluteus maximus muscles"),
            Exercise("overheadPress", "Overhead Press", "A shoulder exercise"),
            Exercise("barbellRow", "BarbellRow", "A posterior chain exercise"),
        )
        fun fetchData(): List<Exercise> {
            return initialDataList
        }
    }
}