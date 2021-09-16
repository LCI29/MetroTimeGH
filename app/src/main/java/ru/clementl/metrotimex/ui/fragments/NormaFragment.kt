package ru.clementl.metrotimex.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.ui.onNavDestinationSelected
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentNormaBinding
import ru.clementl.metrotimex.ui.activities.MainActivity
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.CalendarViewModel
import ru.clementl.metrotimex.viewmodel.NormaViewModel
import ru.clementl.metrotimex.viewmodel.NormaViewModelFactory
import java.time.YearMonth

class NormaFragment : Fragment() {

    private var _binding: FragmentNormaBinding? = null
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private val normaViewModel: NormaViewModel by viewModels {
        NormaViewModelFactory(
            calendarViewModel.allDays.value ?: listOf(),
            (activity as MainActivity).statuses,
//            (activity?.application as MetroTimeApplication).machinistStatusRepository,
            YearMonth.now()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_norma, container, false
        )
        _binding?.viewModel = normaViewModel
        _binding?.lifecycleOwner = this

        setHasOptionsMenu(true)
        val binding = _binding!!


        binding.cellWorkdays.statName.text = stringFrom(R.string.workdays_stat_name)
        normaViewModel.workDayString.observe(viewLifecycleOwner) {
            binding.cellWorkdays.statValue.text = it
        }

        binding.cellWeekends.statName.text = stringFrom(R.string.weekends_stat_name)
        normaViewModel.weekendString.observe(viewLifecycleOwner) {
            binding.cellWeekends.statValue.text = it
        }

        binding.cellSickListDays.statName.text = stringFrom(R.string.sick_list_days_stat_name)
        normaViewModel.sickListDaysString.observe(viewLifecycleOwner) {
            binding.cellSickListDays.statValue.text = it
        }

        binding.cellVacationDays.statName.text = stringFrom(R.string.vacation_days_stat_name)
        normaViewModel.vacationDaysString.observe(viewLifecycleOwner) {
            binding.cellVacationDays.statValue.text = it
        }

        binding.cellNightShifts.statName.text = stringFrom(R.string.night_shifts_stat_name)
        normaViewModel.nightShiftsString.observe(viewLifecycleOwner) {
            binding.cellNightShifts.statValue.text = it
        }

        binding.cellEveningShifts.statName.text = stringFrom(R.string.evening_shifts_stat_name)
        normaViewModel.eveningShiftsString.observe(viewLifecycleOwner) {
            binding.cellEveningShifts.statValue.text = it
        }

        binding.cellEveningHours.statName.text = stringFrom(R.string.evening_hours_stat_name)
        normaViewModel.eveningHoursString.observe(viewLifecycleOwner) {
            binding.cellEveningHours.statValue.text = it
        }

        binding.cellNightHours.statName.text = stringFrom(R.string.night_hours_stat_name)
        normaViewModel.nightHoursString.observe(viewLifecycleOwner) {
            binding.cellNightHours.statValue.text = it
        }

        binding.cellAsMasterHours.statName.text = stringFrom(R.string.as_master_hours_stat_name)
        normaViewModel.masterHoursString.observe(viewLifecycleOwner) {
            binding.cellAsMasterHours.statValue.text = it
        }

        binding.cellAsMentorHours.statName.text = stringFrom(R.string.as_mentor_hours_stat_name)
        normaViewModel.mentorHoursString.observe(viewLifecycleOwner) {
            binding.cellAsMentorHours.statValue.text = it
        }

        binding.countFutureShiftsChb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                normaViewModel.onCheckedCountFuture()
            } else {
                normaViewModel.onUncheckedCountFuture()
            }
        }

        binding.previousButton.setOnClickListener {
            normaViewModel.currentMonth.value?.let {
                normaViewModel.setMonth(it.previous())
            }
        }

        binding.nextButton.setOnClickListener {
            normaViewModel.currentMonth.value?.let {
                normaViewModel.setMonth(it.next())
            }
        }
        return _binding?.root
    }

    private fun stringFrom(resId: Int): String = requireContext().getString(resId)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    // Подключение пунктов оверфлоу-меню к навигации. Id должны совпадать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected((activity as MainActivity).navController)
                || super.onOptionsItemSelected(item)
    }

}