package com.example.finalproject.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.data.Exercise
import com.example.finalproject.ui.main.WorkoutRowAdapter
import com.example.finalproject.databinding.FragmentRvBinding


class WorkoutFragment: Fragment() {
    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: WorkoutRowAdapter
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    private var resultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val returnTitleVal = result.data?.getStringExtra(titleKey)
                val returnSetsVal = result.data?.getIntExtra(setsKey, 3)
                val returnRepsVal = result.data?.getIntExtra(repsKey, 10)
                val returnWeightVal = result.data?.getIntExtra(weightKey, 45)
                Log.d("Dbg", "Reps value changed: $returnRepsVal")
                var currExercise = pageViewModel.listOfWorkouts.value?.find{
                    it.title == returnTitleVal
                }
                //val idx = pageViewModel.listOfWorkouts.value?.indexOf(currExercise)!!
                pageViewModel.modifyWorkoutList(currExercise!!)
                currExercise?.numSets = returnSetsVal!!
                currExercise?.numReps = returnRepsVal!!
                currExercise?.numWeight = returnWeightVal!!
                pageViewModel.modifyWorkoutList(currExercise!!)
                //Set the exercise values to the ones recently fetched in the workout list
                //Adjust the buttons for the sets and reps in the currently clicked list item
            }
        }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): WorkoutFragment {
            return WorkoutFragment().apply {
                arguments = Bundle().apply {
                    putInt(WorkoutFragment.ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
        const val titleKey = "title"
        const val setsKey = "sets"
        const val repsKey = "reps"
        const val weightKey = "weight"
    }

    // Set up the adapter
    private fun initAdapter(binding: FragmentRvBinding) : WorkoutRowAdapter {
        return WorkoutRowAdapter(pageViewModel, this)
    }

    private fun notifyWhenFragmentForegrounded(workoutRowAdapter: WorkoutRowAdapter) {
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(requireActivity()).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(WorkoutFragment.ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")

        adapter = WorkoutRowAdapter(pageViewModel, this)
        _binding?.recyclerView?.adapter = adapter
        _binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        _binding?.recyclerView?.addItemDecoration(DividerItemDecoration
            (_binding?.recyclerView!!.context, LinearLayoutManager.VERTICAL))

        pageViewModel.listOfWorkouts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            Log.d("Dbg", "Detected change in list of workouts")
        })
    }

    fun launchExerciseSettings(currExercise: Exercise) {
        val intent = Intent(this.context, ExerciseSettings::class.java)
        // Launch the intent
        intent.apply {
            putExtra(titleKey, currExercise.title)
            putExtra(setsKey, currExercise.numSets)
            putExtra(repsKey, currExercise.numReps)
            putExtra(weightKey, currExercise.numWeight)
        }
        resultLauncher.launch(intent)
    }
}