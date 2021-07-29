package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import com.takisoft.preferencex.PreferenceFragmentCompat
import ru.clementl.metrotimex.R


class ProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile, rootKey)

    }

    //    var dateLong: Long = 0L
//
//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.profile, rootKey)
//
//        val sincePreference = findPreference<LongPreference>("since")
//        sincePreference?.setOnPreferenceClickListener {
//            showDateDialog(sincePreference.getIt())
//            logd("in OnPrefListener: dateLong = $dateLong")
//            sincePreference.persistIt(dateLong)
//            dateLong = sincePreference.getIt()
//            false
//        }
//        logd("$dateLong")
//
//    }

//    private fun showDateDialog(epochDay: Long) {
//        logd("showDateDialog. epochDay = $epochDay")
//        val now = if (epochDay == 0L) LocalDate.now() else LocalDate.ofEpochDay(epochDay)
//        DatePickerDialog(requireContext(), this, now.year, now.monthValue - 1, now.dayOfMonth).show()
//    }
//
//    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
//        val date = LocalDate.of(p1, p2 + 1, p3)
//        logd("$p1 - ${p2 + 1} - $p3")
//        dateLong = date.toEpochDay()
//        logd("on Date set: $dateLong")
//    }
}

//class LongPreference(context: Context, attributeSet: AttributeSet) : Preference(context, attributeSet) {
//    fun persistIt(value: Long) {
//        persistLong(value)
//    }
//    fun getIt(): Long {
//        return getPersistedLong(0L)
//    }
//}