package ru.clementl.metrotimex.ui.fragments.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.ui.fragments.ShiftCreateFragment
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.ShiftCreateViewModel
import java.time.LocalDate
import java.util.*

class DatePickerFragment(val viewModel: ShiftCreateViewModel) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = viewModel.startDate.value ?: LocalDate.now()
        val y = date.year
        val m = date.month.value - 1 // поскольку внутри используется Calendar
        val d = date.dayOfMonth

        return DatePickerDialog(requireContext(), this, y, m, d)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val month = p2 + 1

        when (tag!!) {
            ShiftCreateFragment.DATE_PICKER -> {
                viewModel.setStartDate(LocalDate.of(p1, month, p3))
                logd("onDateSet: $p1, $month, $p3")
            }
        }
    }
}