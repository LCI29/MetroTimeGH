package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.SwitchPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import ru.clementl.metrotimex.R

const val ON_POST_SINCE_KEY = "on_post_since"
const val QUALIFICATION_CLASS_KEY = "qualification_class"
const val IS_MASTER_KEY = "is_master"
const val IS_MENTOR_KEY = "is_mentor"
const val MONTH_BONUS_KEY = "month_bonus"
const val IN_UNION_KEY = "in_union"

class ProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile, rootKey)

        val qualificationClassPreference =
            findPreference<DropDownPreference>(QUALIFICATION_CLASS_KEY)
        qualificationClassPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val key = preference.key
            true
        }

    }

}
