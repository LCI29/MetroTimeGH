package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentShiftDetailBinding

class ShiftDetailFragment : Fragment() {

    private var binding: FragmentShiftDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShiftDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return inflater.inflate(R.layout.fragment_shift_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}