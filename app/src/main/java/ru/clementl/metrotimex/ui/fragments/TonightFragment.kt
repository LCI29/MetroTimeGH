package ru.clementl.metrotimex.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.converters.fromAmericanToDate
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.databinding.FragmentTonightBinding
import ru.clementl.metrotimex.model.data.Machinist
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.TonightViewModel
import ru.clementl.metrotimex.viewmodel.TonightViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime

class TonightFragment : Fragment() {



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
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val machinist = Machinist(
            onPostSince = prefs.getString("on_post_since", "01/01/2021")?.fromAmericanToDate() ?: LocalDate.of(2021,1,1),
            qualificationClass = prefs.getString("qualification_class", "4")?.toInt() ?: 4,
            isMaster = prefs.getBoolean("is_master", false),
            isMentor = prefs.getBoolean("is_mentor", false),
            monthBonus = prefs.getString("month_bonus", "0.25")?.toDouble()?.div(100) ?: 0.25,
            isInUnion = prefs.getBoolean("in_union", true)
        )

        val tonightViewModel: TonightViewModel by viewModels {
            TonightViewModelFactory((requireActivity().application as MetroTimeApplication).repository, machinist)
        }
        _binding = fragmentBinding
        logd("""
            TonightFragment: ViewModel creation
            
            machinist = $machinist
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