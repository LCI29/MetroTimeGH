package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentNormaBinding
import ru.clementl.metrotimex.viewmodel.CalendarViewModel

class NormaFragment : Fragment() {

    private var _binding: FragmentNormaBinding? = null
    private val calendarViewModel: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_norma, container, false)
        return _binding?.root
    }


}