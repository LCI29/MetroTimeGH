package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentShiftDetailBinding
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.room.MetroTimeDatabase
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.ShiftDetailViewModel
import ru.clementl.metrotimex.viewmodel.ShiftDetailViewModelFactory

class ShiftDetailFragment : Fragment() {

    private var _binding: FragmentShiftDetailBinding? = null
    private lateinit var binding: FragmentShiftDetailBinding
    private lateinit var shiftDetailViewModel: ShiftDetailViewModel
    private lateinit var arguments: ShiftDetailFragmentArgs

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
        val dataSource = (application as MetroTimeApplication).repository
        val viewModelFactory = ShiftDetailViewModelFactory(arguments.dayId, dataSource)

        // Get a reference to the ViewModel associated with this fragment
        shiftDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ShiftDetailViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = shiftDetailViewModel



        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.overflow_detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_option -> deleteDay(arguments.dayId)
            android.R.id.home -> findNavController().navigateUp()
        }
        return true
    }

    private fun deleteDay(dayId: Long) {
        shiftDetailViewModel.deleteDay(dayId)
        logd("before onClose calling")
        findNavController().navigate(
            ShiftDetailFragmentDirections.actionShiftDetailFragmentToCalendarFragment()
        )

    }




}