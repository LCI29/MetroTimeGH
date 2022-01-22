package ru.clementl.metrotimex.screens.calendar

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.SHIFT_CREATING
import ru.clementl.metrotimex.MainActivity
import ru.clementl.metrotimex.screens.SharedViewModel

class CalendarFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()


    // Так создается SharedViewModel с репозиторием (с параметрами)
    private val calendarViewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory(
            (activity?.application as MetroTimeApplication).repository,
            (activity?.application as MetroTimeApplication).machinistStatusRepository
        )
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
        registerForContextMenu(recyclerView)

        calendarViewModel.allDays.observe(viewLifecycleOwner) { days ->
            days?.let { mAdapter.submitList(it) }
        }

        calendarViewModel.allDays.value?.let { list ->
            val d = list.find { it.date == calendarViewModel.today }
            d?.let {
                recyclerView.scrollToPosition(list.indexOf(d))
            }
        }
    }

    private fun setupAdapter() {
        mAdapter = DayStatusListAdapter(DayListener { dateId ->
            calendarViewModel.onShiftClicked(dateId)
        })
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }

    private fun createNewShift() {
        sharedViewModel.currentDay = null
        findNavController().navigate(
            CalendarFragmentDirections.actionCalendarFragmentToShiftEditDialogFragment(
                SHIFT_CREATING
            )
        )
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