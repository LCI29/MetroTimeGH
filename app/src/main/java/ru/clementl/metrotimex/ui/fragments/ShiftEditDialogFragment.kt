package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentShiftEditDialogBinding
import ru.clementl.metrotimex.ui.fragments.pickers.DatePickerFragment
import ru.clementl.metrotimex.ui.fragments.pickers.TimePickerFragment
import ru.clementl.metrotimex.utils.asSimpleDate
import ru.clementl.metrotimex.utils.asSimpleTime
import ru.clementl.metrotimex.utils.showToast
import ru.clementl.metrotimex.viewmodel.ShiftCreateViewModel

class ShiftEditDialogFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private var binding: FragmentShiftEditDialogBinding? = null
    private lateinit var spinner: Spinner
    val createShiftVm: ShiftCreateViewModel by activityViewModels()

    companion object {
        const val DATE_PICKER = "date_picker"
        const val TIME_PICKER_START = "time_picker_start"
        const val TIME_PICKER_END = "time_picker_end"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShiftEditDialogBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // make spinner
        spinner = binding!!.spDayType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.day_status_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        with(binding!!) {
            buttonStartTime.setOnClickListener {
                TimePickerFragment().show(requireActivity().supportFragmentManager, TIME_PICKER_START)
            }

            buttonEndTime.setOnClickListener {
                TimePickerFragment().show(requireActivity().supportFragmentManager, TIME_PICKER_END)
            }

            fieldChooseDate.setOnClickListener {
                DatePickerFragment().show(requireActivity().supportFragmentManager, DATE_PICKER)
            }
        }

        with (createShiftVm) {
            startDate.observe(viewLifecycleOwner) {
                binding!!.fieldChooseDate.text = it.asSimpleDate()
            }
            startTime.observe(viewLifecycleOwner) {
                binding!!.buttonStartTime.text = it.asSimpleTime()
            }
            endTime.observe(viewLifecycleOwner) {
                binding!!.buttonEndTime.text = it.asSimpleTime()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        when (pos) {
            0 -> {
                showToast("0")
                with(binding!!) {
                    etShiftName.visibility = View.VISIBLE
                    tvStartText.visibility = View.VISIBLE
                    tvEndText.visibility = View.VISIBLE
                    buttonStartTime.visibility = View.VISIBLE
                    buttonEndTime.visibility = View.VISIBLE
                    etStartPlace.visibility = View.VISIBLE
                    etEndPlace.visibility = View.VISIBLE
                }
            }
            else -> {
                showToast(pos.toString())
                with(binding!!) {
                    etShiftName.visibility = View.GONE
                    tvStartText.visibility = View.GONE
                    tvEndText.visibility = View.GONE
                    buttonStartTime.visibility = View.GONE
                    buttonEndTime.visibility = View.GONE
                    etStartPlace.visibility = View.GONE
                    etEndPlace.visibility = View.GONE
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        createShiftVm.reset()
    }

}