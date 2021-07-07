package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.ui.adapters.DayStatusAdapter
import ru.clementl.metrotimex.viewmodel.SharedViewModel

class CalendarFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

//    private var binding: FragmentCalendarBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var mAdapter: DayStatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val fragmentBinding = FragmentCalendarBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
//        return binding?.root
        //        view?.findViewById<RecyclerView>(R.id.recycler_view)
        return activity?.layoutInflater?.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { createNewShift() }
        recyclerView = view.findViewById(R.id.recycler_view)
        setupAdapter()
    }

    private fun setupAdapter() {
        val dayStatusAdapter = DayStatusAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = dayStatusAdapter
        dayStatusAdapter.setData(viewModel.dayList)
    }

    private fun createNewShift() {
        findNavController().navigate(R.id.action_calendarFragment_to_shiftEditDialogFragment)
    }


}