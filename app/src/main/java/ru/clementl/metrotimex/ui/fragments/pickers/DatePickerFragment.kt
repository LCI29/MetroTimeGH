package ru.clementl.metrotimex.ui.fragments.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.ui.fragments.ShiftEditDialogFragment
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, y, m, d)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        when (tag!!) {
            ShiftEditDialogFragment.DATE_PICKER -> {
                (activity as MainActivity).shiftCreateViewModel.setStartDate(LocalDate.of(p1, p2 + 1, p3))
                logd("onDateSet: $p1, $p2, $p3")
            }
        }
    }
}