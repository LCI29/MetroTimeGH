package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.SHIFT_EDITING
import ru.clementl.metrotimex.databinding.FragmentShiftDetailBinding
import ru.clementl.metrotimex.model.data.finalSalary
import ru.clementl.metrotimex.model.data.isFinished
import ru.clementl.metrotimex.model.data.isShift
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.salaryStyle
import ru.clementl.metrotimex.viewmodel.CalendarViewModel
import ru.clementl.metrotimex.viewmodel.SharedViewModel
import ru.clementl.metrotimex.viewmodel.ShiftDetailViewModel
import ru.clementl.metrotimex.viewmodel.ShiftDetailViewModelFactory
import java.lang.Exception

class ShiftDetailFragment : Fragment() {

    private var _binding: FragmentShiftDetailBinding? = null
    private lateinit var binding: FragmentShiftDetailBinding
    private lateinit var shiftDetailViewModel: ShiftDetailViewModel
    private lateinit var arguments: ShiftDetailFragmentArgs
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShiftDetailBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        binding = _binding!!

        val application = requireNotNull(activity).application
        arguments = ShiftDetailFragmentArgs.fromBundle(requireArguments())

        // Create an instance of the ViewModel Factory
        val calendarRepository = (application as MetroTimeApplication).repository
        val machinistStatusRepository = application.machinistStatusRepository


        val viewModelFactory =
            ShiftDetailViewModelFactory(arguments.dayId, calendarRepository, machinistStatusRepository)

        // Get a reference to the ViewModel associated with this fragment
        shiftDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ShiftDetailViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = shiftDetailViewModel

        shiftDetailViewModel.getDay().observe(viewLifecycleOwner, { day ->
            sharedViewModel.currentDay = day
            shiftDetailViewModel.getStatus().observe(viewLifecycleOwner) { status ->
                if (day.isShift() && day.timeSpan.isFinished()) {
                    binding.earnedText.text = day.finalSalary(status).salaryStyle()
                } else {
                    binding.earnedText.visibility = View.GONE
                }
            }
        })

        setHasOptionsMenu(true)


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.overflow_detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_option -> deleteDay(arguments.dayId)
            android.R.id.home -> findNavController().navigateUp()
            R.id.edit_option -> editDay()
        }
        return true
    }

    private fun editDay() {
        findNavController().navigate(
            ShiftDetailFragmentDirections.actionShiftDetailFragmentToShiftEditDialogFragment(
                SHIFT_EDITING
            )
        )
    }

    private fun deleteDay(dayId: Long) {
        shiftDetailViewModel.deleteDay(dayId)
        findNavController().navigate(
            ShiftDetailFragmentDirections.actionShiftDetailFragmentToCalendarFragment()
        )
    }


}