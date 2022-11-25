package com.example.finalproject.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.ui.main.WorkoutRowAdapter
import com.example.finalproject.databinding.FragmentRvBinding


class WorkoutFragment: Fragment() {
    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: WorkoutRowAdapter
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): WorkoutFragment {
            return WorkoutFragment().apply {
                arguments = Bundle().apply {
                    putInt(WorkoutFragment.ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    // Set up the adapter
    private fun initAdapter(binding: FragmentRvBinding) : WorkoutRowAdapter {
        return WorkoutRowAdapter(pageViewModel)
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

        adapter = WorkoutRowAdapter(pageViewModel)
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
}