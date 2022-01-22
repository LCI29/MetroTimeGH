package ru.clementl.metrotimex.screens.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.utils.showToast
import ru.clementl.metrotimex.utils.stringFrom

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<ListPreference>("theme_pref")?.setOnPreferenceChangeListener { _, _ ->
            requireActivity().recreate()
            true
        }
    }
}