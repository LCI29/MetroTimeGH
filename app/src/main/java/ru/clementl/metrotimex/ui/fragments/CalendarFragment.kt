package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.ui.adapters.DayListener
import ru.clementl.metrotimex.ui.adapters.DayStatusListAdapter
import ru.clementl.metrotimex.utils.showToast
import ru.clementl.metrotimex.viewmodel.CalendarViewModel
import ru.clementl.metrotimex.viewmodel.CalendarViewModelFactory

class CalendarFragment : Fragment() {


    // Так создается ViewModel с репозиторием (с параметрами)
    private val calendarViewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory((activity?.application as MetroTimeApplication).repository)
    }

//    private var binding: FragmentCalendarBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var mAdapter: DayStatusListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val fragmentBinding = FragmentCalendarBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
//        return binding?.root
        //        view?.findViewById<RecyclerView>(R.id.recycler_view)
        calendarViewModel.navigateToShiftDetail.observe(viewLifecycleOwner, Observer { dayId ->
            dayId?.let {
                findNavController().navigate(
                    CalendarFragmentDirections.actionCalendarFragmentToShiftDetailFragment(dayId)
                )
                calendarViewModel.onShiftDetailNavigated()
            }
        })

        setHasOptionsMenu(true)

        return activity?.layoutInflater?.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { createNewShift() }
        recyclerView = view.findViewById(R.id.recycler_view)
        setupAdapter()

        calendarViewModel.allDays.observe(viewLifecycleOwner) { days ->
            days?.let { mAdapter.submitList(it) }
        }
    }

    private fun setupAdapter() {
        mAdapter = DayStatusListAdapter(DayListener {
            dateId ->
            calendarViewModel.onShiftClicked(dateId)
        })
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }

    private fun createNewShift() {
        findNavController().navigate(R.id.action_calendarFragment_to_shiftEditDialogFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    // Подключение пунктов оверфлоу-меню к навигации. Id должны совпадать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected((activity as MainActivity).navController)
                || super.onOptionsItemSelected(item)
    }



}