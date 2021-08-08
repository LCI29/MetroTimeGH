package ru.clementl.metrotimex.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentTonightBinding
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.TonightViewModel
import ru.clementl.metrotimex.viewmodel.TonightViewModelFactory

class TonightFragment : Fragment() {

    private val tonightViewModel: TonightViewModel by viewModels {
        TonightViewModelFactory((requireActivity().application as MetroTimeApplication).repository)
    }

    private var _binding: FragmentTonightBinding? = null
    private val binding: FragmentTonightBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding: FragmentTonightBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tonight, container, false
        )
        _binding = fragmentBinding
        logd("""
            TonightFragment: ViewModel creation
            Loaded list:
            ${tonightViewModel.getCalendar()}
        """.trimIndent())

        binding.viewModel = tonightViewModel
        binding.lifecycleOwner = this

        logd("""
            SimpleState on initialize = ${tonightViewModel.simpleState.value?.desc}
        """.trimIndent())

        tonightViewModel.advancedState.observe(viewLifecycleOwner) {
            with(binding) {
                nextShiftCell.day = tonightViewModel.nextShift
                currentShiftLayout.day = tonightViewModel.today
                currentNonShiftLayout.day = tonightViewModel.today
                when (it) {
                    is ShiftAdvancedState -> {
                        currentNonShiftLayout.rootLayout.visibility = View.GONE
                        currentShiftLayout.rootLayout.visibility = View.VISIBLE
                        earnedNow.visibility = View.VISIBLE
                        earnedNow.setTextColor(Color.BLACK)
                        finishedText.visibility = View.GONE
                        nextShiftLayout.visibility = View.VISIBLE
                    }
                    is AfterShiftAdvancedState -> {
                        currentNonShiftLayout.rootLayout.visibility = View.GONE
                        currentShiftLayout.rootLayout.visibility = View.VISIBLE
                        earnedNow.visibility = View.VISIBLE
                        earnedNow.setTextColor(Color.GRAY)
                        finishedText.visibility = View.VISIBLE
                        nextShiftLayout.visibility = View.VISIBLE
                    }
                    is BeforeShiftAdvancedState -> {
                        currentNonShiftLayout.rootLayout.visibility = View.GONE
                        currentShiftLayout.rootLayout.visibility = View.GONE
                        earnedNow.visibility = View.GONE
                        finishedText.visibility = View.GONE
                        nextShiftLayout.visibility = View.VISIBLE
                    }
                    is SpecialAdvancedState -> {
                        currentShiftLayout.rootLayout.visibility = View.GONE
                        currentNonShiftLayout.rootLayout.visibility = View.VISIBLE
                        earnedNow.visibility = View.GONE
                        finishedText.visibility = View.GONE
                        nextShiftLayout.visibility = View.VISIBLE
                    }
                    is NoDataAdvancedState -> {
                        currentNonShiftLayout.rootLayout.visibility = View.GONE
                        currentShiftLayout.rootLayout.visibility = View.GONE
                        earnedNow.visibility = View.GONE
                        finishedText.visibility = View.GONE
                        nextShiftLayout.visibility = View.VISIBLE
                    }

                }
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}