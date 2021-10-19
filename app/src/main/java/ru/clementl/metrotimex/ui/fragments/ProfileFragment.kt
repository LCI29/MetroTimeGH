package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.takisoft.preferencex.DatePickerPreference
import com.takisoft.preferencex.EditTextPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.utils.logd

const val ON_POST_SINCE_KEY = "on_post_since"
const val QUALIFICATION_CLASS_KEY = "qualification_class"
const val IS_MASTER_KEY = "is_master"
const val IS_MENTOR_KEY = "is_mentor"
const val MONTH_BONUS_KEY = "month_bonus"
const val IN_UNION_KEY = "in_union"
const val RATE_PER_HOUR_KEY = "rate_per_hour"
class ProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile, rootKey)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val repository = (requireActivity().application as MetroTimeApplication).machinistStatusRepository

        val machinistPreferences = setOf(
            findPreference<DatePickerPreference>(ON_POST_SINCE_KEY),
            findPreference<DropDownPreference>(QUALIFICATION_CLASS_KEY),
            findPreference<SwitchPreference>(IS_MASTER_KEY),
            findPreference<SwitchPreference>(IS_MENTOR_KEY),
            findPreference<EditTextPreference>(MONTH_BONUS_KEY),
            findPreference<SwitchPreference>(IN_UNION_KEY),
            findPreference<EditTextPreference>(RATE_PER_HOUR_KEY)
        )

        for (preference in machinistPreferences) {
            preference?.setOnPreferenceChangeListener { preference, _ ->
                CoroutineScope(Job() + Dispatchers.Default).launch {
                    repository.insert(MachinistStatus.create(
                        prefs.machinist(),
                        ratePerHour = prefs.ratePerHour()
                    ))
                }
                logd("$preference changed")
                true
            }
        }

    }

}
