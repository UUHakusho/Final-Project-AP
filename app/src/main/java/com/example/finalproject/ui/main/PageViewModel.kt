package com.example.finalproject.ui.main

import androidx.lifecycle.*
import com.example.finalproject.data.Exercise
import com.example.finalproject.data.Repository
import kotlinx.coroutines.launch

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    private var title = MutableLiveData<String>()

    var listOfWorkouts = MutableLiveData<List<Exercise>>().apply {
        value = listOf()
    }
    var listOfExercises = MutableLiveData<List<Exercise>>().apply {
        value = listOf()
    }

    var mediatorListOfWorkouts = MediatorLiveData<List<Exercise>>().apply {
        value = listOfWorkouts.value
        addSource(listOfWorkouts) {
            value = listOfWorkouts.value
        }
    }

    var mediatorListOfExercises = MediatorLiveData<List<Exercise>>().apply {
        value = listOfExercises.value
        addSource(listOfExercises) {
            value = listOfExercises.value
        }
    }

    fun modifyWorkoutList(exercise: Exercise) {
        listOfWorkouts.value?.toMutableList()?.let { woList ->
            if(woList.contains(exercise)) {
                woList.remove(exercise)
            } else {
                woList.add(exercise)
            }
            listOfWorkouts.value = woList
        }
    }

    fun observeWorkoutList(): List<Exercise>? {
        return listOfWorkouts.value
    }

    init {
        viewModelScope.launch {
            // listOfWorkouts.value = Repository.fetchData()
            listOfExercises.value = Repository.fetchData()
        }
    }

}