package com.example.finalproject.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.data.Exercise
import com.example.finalproject.data.Repository
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
                val returnMinsVal = result.data?.getIntExtra(minsKey, 1)
                val returnSecVal = result.data?.getIntExtra(secKey, 30)
                Log.d("Dbg", "Reps value changed: $returnRepsVal")
                var currExercise = adapter.currentList.find{
                    it.title == returnTitleVal
                }

                currExercise?.numSets = returnSetsVal!!
                currExercise?.numReps = returnRepsVal!!
                currExercise?.numWeight = returnWeightVal!!
                currExercise?.numRestMin = returnMinsVal!!
                currExercise?.numRestSec = returnSecVal!!
                pageViewModel.listOfWorkouts.postValue(pageViewModel.listOfWorkouts.value)
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
        const val keyKey = "key"
        const val titleKey = "title"
        const val setsKey = "sets"
        const val repsKey = "reps"
        const val weightKey = "weight"
        const val minsKey = "restMins"
        const val secKey = "restSec"
    }

    private fun notifyWhenFragmentForegrounded(workoutRowAdapter: WorkoutRowAdapter) {
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(requireActivity())[PageViewModel::class.java].apply {
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

        //adapter = WorkoutRowAdapter(pageViewModel, this)
        adapter = WorkoutRowAdapter(pageViewModel, this,
            object : WorkoutRowAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                exerciseSettingsClick(position)
            }
        })
        _binding?.recyclerView?.adapter = adapter
        _binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        _binding?.recyclerView?.addItemDecoration(DividerItemDecoration
            (_binding?.recyclerView!!.context, LinearLayoutManager.VERTICAL))

        binding.btnSaveWrkt.setOnClickListener {
            Repository.saveLatestExerciseDefaults(pageViewModel.listOfWorkouts.value!!)
            Repository.addWorkoutItem(pageViewModel.listOfWorkouts.value!!)
            pageViewModel.listOfWorkouts.postValue(listOf())
            for(idx in 0 until adapter.currentList.size) {
                adapter.clearAllExerciseData(idx)
            }
        }

        pageViewModel.listOfWorkouts.observe(viewLifecycleOwner, Observer {
            // Check if all exercises in the workout are complete
            var workoutComplete = true
            for(exercise in pageViewModel.listOfWorkouts.value!!) {
                for(idx in 0 until exercise.numSets) {
                    if(!exercise.isComplete[idx]) {
                        workoutComplete = false
                    }
                }
            }
            if(pageViewModel.listOfWorkouts.value!!.isNotEmpty() && workoutComplete) {
                Log.d("Dbg","Workout completion detected")
                binding.btnSaveWrkt.isEnabled = true

            }
            else {
                binding.btnSaveWrkt.isEnabled = false
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            Log.d("Dbg", "Detected change in list of workouts")
        })
    }

    private fun exerciseSettingsClick(position: Int) {
        val popupMenu = PopupMenu(activity,
            binding.recyclerView[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.settings -> {
                        launchExerciseSettings(adapter.currentList[position])
                        return true
                    }
                    R.id.clear -> {
                        adapter.clearAllExerciseData(position)
                        pageViewModel.listOfWorkouts.postValue(pageViewModel.listOfWorkouts.value)
                        return true
                    }
                    R.id.delete -> {
                        adapter.clearAllExerciseData(position)
                        pageViewModel.modifyWorkoutList(adapter.currentList[position])
                        return true
                    }
                    R.id.progress -> {
                        val intent = Intent(context, PlotViewActivity::class.java)
                        intent.apply {
                            putExtra(keyKey, adapter.currentList[position].key)
                            putExtra(titleKey, adapter.currentList[position].title)
                        }
                        context?.startActivity(intent)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    fun launchExerciseSettings(currExercise: Exercise) {
        val intent = Intent(this.context, ExerciseSettings::class.java)
        intent.apply {
            putExtra(titleKey, currExercise.title)
            putExtra(setsKey, currExercise.numSets)
            putExtra(repsKey, currExercise.numReps)
            putExtra(weightKey, currExercise.numWeight)
            putExtra(minsKey, currExercise.numRestMin)
            putExtra(secKey, currExercise.numRestSec)
        }
        resultLauncher.launch(intent)
    }
}