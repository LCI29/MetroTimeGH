package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.clementl.metrotimex.R

class ProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile, rootKey)
    }
}