package ru.clementl.metrotimex.ui.fragments.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.ui.fragments.ShiftCreateFragment
import ru.clementl.metrotimex.utils.logd
import java.time.LocalTime
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val activity = activity as MainActivity
        when(tag!!) {
            ShiftCreateFragment.TIME_PICKER_START -> {
                activity.shiftCreateViewModel.setStartTime(LocalTime.of(p1, p2))
                logd("onTimeSet: Start = $p1, $p2")
            }
            ShiftCreateFragment.TIME_PICKER_END -> {
                activity.shiftCreateViewModel.setEndTime(LocalTime.of(p1, p2))
                logd("onTimeSet: End = $p1, $p2")
            }
        }
    }
}