package ru.clementl.metrotimex.screens.yearly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.FragmentYearBonusBinding
import ru.clementl.metrotimex.model.data.getYearlyBonusQ
import ru.clementl.metrotimex.screens.tonight.machinist
import ru.clementl.metrotimex.utils.salaryStyle

class YearBonusFragment : Fragment() {

    private val app by lazy { requireActivity().application as MetroTimeApplication }
    private var _binding: FragmentYearBonusBinding? = null
    private val viewModel: YearBonusViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_year_bonus, container, false
        )
        _binding?.viewModel = viewModel
        _binding?.lifecycleOwner = this
        val binding = _binding!!

        val machinist = app.prefs.machinist()
        val q = machinist.getYearlyBonusQ()

        fun update() {
            viewModel.calculateBonus(q, machinist.isInUnion)
        }

        binding.yearlyStageQText.text = getString(R.string.year_stage_q_text, q)

        binding.yearlySumEt.doOnTextChanged { text, _, _, _ ->
            val sgp = text?.toString()?.toDoubleOrNull() ?: 0.0
            viewModel.setSgp(sgp)
            update()
        }

        binding.plusButton.setOnClickListener {
            viewModel.yearQInc()
            update()
        }

        binding.minusButton.setOnClickListener {
            viewModel.yearQDec()
            update()
        }

        viewModel.yearQ.observe(viewLifecycleOwner) {
            binding.textYearlyQ.text = getString(R.string.yearly_q_text, it)
        }

        viewModel.result.observe(viewLifecycleOwner) {
            binding.yearlyResult.text = it.salaryStyle()
        }


        return binding.root
    }

}
