package ru.clementl.metrotimex.ui.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.RATE_PER_HOUR_DEFAULT
import ru.clementl.metrotimex.converters.fromAmericanToDate
import ru.clementl.metrotimex.databinding.FragmentTonightBinding
import ru.clementl.metrotimex.model.data.Machinist
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.toDoubleEmptyZero
import ru.clementl.metrotimex.viewmodel.TonightViewModel
import ru.clementl.metrotimex.viewmodel.TonightViewModelFactory
import java.lang.Exception
import java.time.LocalDate

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
        setHasOptionsMenu(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val machinist = prefs.machinist()
        val machinistStatus = MachinistStatus.create(machinist, ratePerHour = prefs.ratePerHour())

        val tonightViewModel: TonightViewModel by viewModels {
            TonightViewModelFactory(
                (requireActivity().application as MetroTimeApplication).repository,
                machinistStatus
            )
        }
        _binding = fragmentBinding
        logd(
            """
            TonightFragment: ViewModel creation
            
            machinist = $machinist
        """.trimIndent()
        )

        binding.viewModel = tonightViewModel
        binding.lifecycleOwner = this

        logd(
            """
            SimpleState on initialize = ${tonightViewModel.simpleState.value?.desc}
        """.trimIndent()
        )

        tonightViewModel.advancedState.observe(viewLifecycleOwner) {
            tonightViewModel.changeCounter()
            logd("AdvancedState is ............ ${it.description}")
            with(binding) {
                nextShiftCell.day = tonightViewModel.nextShift
                textView.text = "Следующая смена ${tonightViewModel.nextShiftTomorrowText}"
                currentShiftLayout.day = tonightViewModel.today
                currentNonShiftLayout.day = tonightViewModel.today
                when (it) {
                    is ShiftAdvancedState -> {
                        currentNonShiftLayout.rootLayout.visibility = View.GONE
                        currentShiftLayout.rootLayout.visibility = View.VISIBLE
                        earnedNow.visibility = View.VISIBLE
                        earnedNow.setTextColor(resources.getColor(R.color.primary_text))
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

        val nextShiftClickListener = View.OnClickListener {
            tonightViewModel.nextShift?.let {
                findNavController().navigate(
                    TonightFragmentDirections.actionTonightFragmentToShiftDetailFragment(
                        it.dateLong
                    )
                )
            }
        }

        val currentShiftClickListener = View.OnClickListener {
            tonightViewModel.today?.let {
                findNavController().navigate(
                    TonightFragmentDirections.actionTonightFragmentToShiftDetailFragment(
                        it.dateLong
                    )
                )
            }
        }

        binding.nextShiftCell.apply {
            shiftName.setOnClickListener(nextShiftClickListener)
            date.setOnClickListener(nextShiftClickListener)
            shiftDescString.setOnClickListener(nextShiftClickListener)
        }

        binding.currentShiftLayout.apply {
            shiftName.setOnClickListener(currentShiftClickListener)
            date.setOnClickListener(currentShiftClickListener)
            shiftDescString.setOnClickListener(currentShiftClickListener)
        }

        binding.currentNonShiftLayout.apply {
            shiftName.setOnClickListener(currentShiftClickListener)
            date.setOnClickListener(currentShiftClickListener)
            dayOfWeek.setOnClickListener(currentShiftClickListener)
        }
        binding.textView.setOnClickListener(nextShiftClickListener)
        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    // Подключение пунктов оверфлоу-меню к навигации. Id должны совпадать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected((activity as MainActivity).navController)
                || super.onOptionsItemSelected(item)
    }
}

fun SharedPreferences.machinist(): Machinist = Machinist(
    onPostSince = getString("on_post_since", "01/01/2021")?.fromAmericanToDate()
        ?: LocalDate.of(2021, 1, 1),
    qualificationClass = getString("qualification_class", "4")?.toInt() ?: 4,
    isMaster = getBoolean("is_master", false),
    isMentor = getBoolean("is_mentor", false),
    monthBonus = getString("month_bonus", "0.25")?.toDoubleEmptyZero()?.div(100) ?: 0.25,
    isInUnion = getBoolean("in_union", true)
)

fun SharedPreferences.ratePerHour(): Double =
    getString("rate_per_hour", RATE_PER_HOUR_DEFAULT.toFloat().toString())?.toDoubleEmptyZero() ?: throw Exception("No rate set")