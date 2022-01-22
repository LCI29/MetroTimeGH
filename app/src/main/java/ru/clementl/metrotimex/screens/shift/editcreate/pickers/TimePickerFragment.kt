package ru.clementl.metrotimex.screens.shift.editcreate.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import ru.clementl.metrotimex.DEFAULT_END_HOUR
import ru.clementl.metrotimex.DEFAULT_MINUTE
import ru.clementl.metrotimex.DEFAULT_START_HOUR
import ru.clementl.metrotimex.screens.shift.editcreate.ShiftCreateFragment
import ru.clementl.metrotimex.screens.shift.editcreate.ShiftCreateViewModel
import ru.clementl.metrotimex.utils.logd
import java.time.LocalTime

class TimePickerFragment(val viewModel: ShiftCreateViewModel) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = when (tag) {
            ShiftCreateFragment.TIME_PICKER_START -> viewModel.startTime.value?.hour ?: DEFAULT_START_HOUR
            ShiftCreateFragment.TIME_PICKER_END -> viewModel.endTime.value?.hour ?: DEFAULT_END_HOUR
            else -> DEFAULT_START_HOUR
        }
        val minute = when(tag) {
            ShiftCreateFragment.TIME_PICKER_START -> viewModel.startTime.value?.minute ?: DEFAULT_MINUTE
            ShiftCreateFragment.TIME_PICKER_END -> viewModel.endTime.value?.minute ?: DEFAULT_MINUTE
            else -> DEFAULT_MINUTE
        }

        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        when(tag) {
            ShiftCreateFragment.TIME_PICKER_START -> {
                viewModel.setStartTime(LocalTime.of(p1, p2))
                logd("onTimeSet: Start = $p1, $p2")
            }
            ShiftCreateFragment.TIME_PICKER_END -> {
                viewModel.setEndTime(LocalTime.of(p1, p2))
                logd("onTimeSet: End = $p1, $p2")
            }
        }
    }
}