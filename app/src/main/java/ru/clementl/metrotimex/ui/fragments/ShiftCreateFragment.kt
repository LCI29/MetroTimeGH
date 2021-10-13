package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.databinding.FragmentShiftCreateBinding
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.utils.*
import ru.clementl.metrotimex.viewmodel.CalendarViewModel
import ru.clementl.metrotimex.viewmodel.SharedViewModel
import ru.clementl.metrotimex.viewmodel.ShiftCreateViewModel
import ru.clementl.metrotimex.viewmodel.ShiftCreateViewModelFactory
import java.time.LocalDate
import java.time.LocalTime

/**
 * Shift create OR edit fragment
 */

class ShiftCreateFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private var binding: FragmentShiftCreateBinding? = null
    private lateinit var spinner: Spinner
    private lateinit var shiftCreateViewModel: ShiftCreateViewModel
    private lateinit var arguments: ShiftCreateFragmentArgs
    val calendarViewModel: CalendarViewModel by activityViewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()





    companion object {
        const val DATE_PICKER = "date_picker"
        const val DATE_RANGE_PICKER = "date_range_picker"
        const val TIME_PICKER_START = "time_picker_start"
        const val TIME_PICKER_END = "time_picker_end"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShiftCreateBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val application = requireNotNull(activity).application
        arguments = ShiftCreateFragmentArgs.fromBundle(requireArguments())

        val dataSource = (application as MetroTimeApplication).repository
        val viewModelFactory = ShiftCreateViewModelFactory(
            dataSource, arguments.mode, sharedViewModel.currentDay)

        shiftCreateViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ShiftCreateViewModel::class.java)
        binding!!.viewModel = shiftCreateViewModel

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
//                logd("on button current day${shiftCreateViewModel.editingDay?.date?.asSimpleDate()}")
//                TimePickerFragment(shiftCreateViewModel).show(requireActivity().supportFragmentManager, TIME_PICKER_START)
                showTimePicker(TIME_PICKER_START)

            }

            buttonEndTime.setOnClickListener {
//                TimePickerFragment(shiftCreateViewModel).show(requireActivity().supportFragmentManager, TIME_PICKER_END)
                showTimePicker(TIME_PICKER_END)
            }

            fieldChooseDate.setOnClickListener {
//                DatePickerFragment(shiftCreateViewModel)
//                    .show(requireActivity().supportFragmentManager, DATE_PICKER)
                showDatePicker()
            }

            fieldChooseDateRange.setOnClickListener {
                showDateRangePicker()
            }
            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
            reserveCheckBox.setOnClickListener {
                shiftCreateViewModel.onReserveChecked(reserveCheckBox.isChecked)
            }
            atzCheckBox.setOnClickListener {
                shiftCreateViewModel.onAtzChecked(atzCheckBox.isChecked)
            }

        }

        with (shiftCreateViewModel) {
            initializeStartAndEndDate()
            startDate.observe(viewLifecycleOwner) {
                binding!!.fieldChooseDate.text = it.ofPattern("d MMMM yyyy, EE")
                binding!!.fieldChooseDateRange.text = "c ${it.ofPattern("dd.MM")} по ${endDate.value?.ofPattern("dd.MM")}"
            }
            endDate.observe(viewLifecycleOwner) {
                binding!!.fieldChooseDateRange.text = "c ${startDate.value?.ofPattern("dd.MM")} по ${it.ofPattern("dd.MM")}"
            }
            startTime.observe(viewLifecycleOwner) {
                binding!!.buttonStartTime.text = it.asSimpleTime()
            }
            endTime.observe(viewLifecycleOwner) {
                binding!!.buttonEndTime.text = it.asSimpleTime()
            }
            workDayTypeLive.observe(viewLifecycleOwner, {
                spinner.setSelection(it.toInt())
            })
            isReserveLive.observe(viewLifecycleOwner, {
                binding!!.reserveCheckBox.isChecked = it
            })
            hasAtzLive.observe(viewLifecycleOwner, {
                binding!!.atzCheckBox.isChecked = it
            })
        }

        binding!!.saveButton.setOnClickListener {
            save()
        }

        return fragmentBinding.root
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.choose_date))
            .setSelection(shiftCreateViewModel.startDate.value?.plusDays(1)?.toLong())
            .build()

        picker.apply {
            addOnPositiveButtonClickListener {
                shiftCreateViewModel.setStartDate(it.toDate())
            }
        }
        picker.show(requireActivity().supportFragmentManager, DATE_PICKER)
    }

    private fun showDateRangePicker() {
        val start = shiftCreateViewModel.startDate.value?.plusDays(1) ?: LocalDate.now()
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Выберите период")
            .setSelection(
                Pair.create(start.toLong(), start.plusDays(1).toLong())
            )
            .build()

        picker.apply {
            addOnPositiveButtonClickListener {
                shiftCreateViewModel.setStartDate(it.first.toDate())
                shiftCreateViewModel.setEndDate(it.second.toDate())
            }
        }
        picker.show(requireActivity().supportFragmentManager, DATE_RANGE_PICKER)
    }

    // call to show Material Time Picker Dialog
    private fun showTimePicker(tag: String) {
        val h = when (tag) {
            TIME_PICKER_START -> shiftCreateViewModel.startTime.value?.hour ?: DEFAULT_START_HOUR
            TIME_PICKER_END -> shiftCreateViewModel.endTime.value?.hour ?: DEFAULT_END_HOUR
            else -> DEFAULT_START_HOUR
        }
        val m = when(tag) {
            TIME_PICKER_START -> shiftCreateViewModel.startTime.value?.minute ?: DEFAULT_MINUTE
            TIME_PICKER_END -> shiftCreateViewModel.endTime.value?.minute ?: DEFAULT_MINUTE
            else -> DEFAULT_MINUTE
        }
        val title = when(tag) {
            TIME_PICKER_START -> resources.getString(R.string.shift_start)
            TIME_PICKER_END -> resources.getString(R.string.shift_end)
            else -> ""
        }
        val picker = MaterialTimePicker.Builder()
            .setInputMode(INPUT_MODE_KEYBOARD)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(h)
            .setMinute(m)
            .setTitleText(title)
            .build()

        picker.apply {
            addOnPositiveButtonClickListener {
                when(tag) {
                    TIME_PICKER_START -> {
                        shiftCreateViewModel.setStartTime(LocalTime.of(hour, minute))
                    }
                    TIME_PICKER_END -> {
                        shiftCreateViewModel.setEndTime(LocalTime.of(hour, minute))
                    }
                }
            }
        }
        picker.show(requireActivity().supportFragmentManager, tag)

    }

    // call to show BetterPickers Time Picker Dialog
//    private fun showAlternativeTimePicker(tag: String) {
//        val tpb = TimePickerBuilder()
//            .setFragmentManager(requireActivity().supportFragmentManager)
//            .setStyleResId(R.style.BetterPickersDialogFragment)
//
//        tpb.addTimePickerDialogHandler { reference, hourOfDay, minute ->
//            when (tag) {
//                TIME_PICKER_START -> {
//                    shiftCreateViewModel.setStartTime(LocalTime.of(hourOfDay, minute))
//                }
//                TIME_PICKER_END -> {
//                    shiftCreateViewModel.setEndTime(LocalTime.of(hourOfDay, minute))
//                }
//            }
//        }
//
//
//        tpb.show()
//    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        when (pos) {
            0 -> {
                shiftCreateViewModel.onWorkDayTypeChanged(WorkDayType.SHIFT)
                with(binding!!) {
                    fieldChooseDate.visibility = View.VISIBLE
                    fieldChooseDateRange.visibility = View.INVISIBLE
                    etShiftName.visibility = View.VISIBLE
                    etShiftNameLayout.visibility = View.VISIBLE
                    tvStartText.visibility = View.VISIBLE
                    tvEndText.visibility = View.VISIBLE
                    buttonStartTime.visibility = View.VISIBLE
                    buttonEndTime.visibility = View.VISIBLE
                    etStartPlace.visibility = View.VISIBLE
                    etEndPlace.visibility = View.VISIBLE
                    reserveCheckBox.visibility = View.VISIBLE
                    atzCheckBox.visibility = View.VISIBLE
                    notesTextInputLayout.visibility = View.VISIBLE
                }
            }
            2, 3 -> {
                with(shiftCreateViewModel) {
                    when (pos) {
                        2 -> onWorkDayTypeChanged(WorkDayType.SICK_LIST)
                        3 -> onWorkDayTypeChanged(WorkDayType.VACATION_DAY)
                    }
                }
                with(binding!!) {
                    if (shiftCreateViewModel.mode == SHIFT_EDITING) {
                        fieldChooseDate.visibility = View.VISIBLE
                        fieldChooseDateRange.visibility = View.INVISIBLE
                        notesTextInputLayout.visibility = View.VISIBLE
                    } else {
                        fieldChooseDateRange.visibility = View.VISIBLE
                        fieldChooseDate.visibility = View.INVISIBLE
                        notesTextInputLayout.visibility = View.INVISIBLE
                    }
                    etShiftName.visibility = View.GONE
                    etShiftNameLayout.visibility = View.GONE
                    tvStartText.visibility = View.GONE
                    tvEndText.visibility = View.GONE
                    buttonStartTime.visibility = View.GONE
                    buttonEndTime.visibility = View.GONE
                    etStartPlace.visibility = View.GONE
                    etEndPlace.visibility = View.GONE
                    reserveCheckBox.visibility = View.GONE
                    atzCheckBox.visibility = View.GONE
                }
            }
            else -> {
                with(shiftCreateViewModel) {
                    when (pos) {
                        1 -> onWorkDayTypeChanged(WorkDayType.WEEKEND)
                        4 -> onWorkDayTypeChanged(WorkDayType.MEDIC_DAY)
                        5 -> onWorkDayTypeChanged(WorkDayType.DONOR_DAY)

                    }
                }
                with(binding!!) {
                    fieldChooseDate.visibility = View.VISIBLE
                    fieldChooseDateRange.visibility = View.INVISIBLE
                    etShiftName.visibility = View.GONE
                    etShiftNameLayout.visibility = View.GONE
                    tvStartText.visibility = View.GONE
                    tvEndText.visibility = View.GONE
                    buttonStartTime.visibility = View.GONE
                    buttonEndTime.visibility = View.GONE
                    etStartPlace.visibility = View.GONE
                    etEndPlace.visibility = View.GONE
                    reserveCheckBox.visibility = View.GONE
                    atzCheckBox.visibility = View.GONE
                    notesTextInputLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        shiftCreateViewModel.reset()
    }

    private fun save() {
        when (shiftCreateViewModel.workDayTypeLive.value) {
            WorkDayType.SICK_LIST, WorkDayType.VACATION_DAY -> {
                if (shiftCreateViewModel.mode == SHIFT_EDITING) saveDay() else saveDays()
            }
            else -> saveDay()
        }
    }

    /**
     * Saves day and shift in db.
     */
    private fun saveDay() {
        if (arguments.mode == SHIFT_EDITING) {
            calendarViewModel.delete(shiftCreateViewModel.editingDay?.dateLong ?: NO_DAY_ID)
        }

        val binding = binding!!
        val day = shiftCreateViewModel.createDay(
            name = binding.etShiftName.text.toString(),
            startLoc = binding.etStartPlace.text.toString(),
            endLoc = binding.etEndPlace.text.toString(),
            notes = binding.notesTextInputEditText.text?.toString()
        )
        calendarViewModel.insert(day)
        findNavController().navigate(R.id.action_shiftEditDialogFragment_to_calendarFragment)
    }

    /**
     * Saves vacation or sick list days in db.
     */
    private fun saveDays() {
        if (arguments.mode == SHIFT_EDITING) {
            calendarViewModel.delete(shiftCreateViewModel.editingDay?.dateLong ?: NO_DAY_ID)
        }

        val binding = binding!!
        val days = shiftCreateViewModel.createDaysInRange()
        for (day in days) {
            calendarViewModel.insert(day)
        }
        findNavController().navigate(R.id.action_shiftEditDialogFragment_to_calendarFragment)
    }

}