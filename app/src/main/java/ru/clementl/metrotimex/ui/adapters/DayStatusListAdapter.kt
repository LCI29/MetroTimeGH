package ru.clementl.metrotimex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.CellShiftBinding
import ru.clementl.metrotimex.databinding.CellWeekendBinding
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TYPE_SHIFT
import ru.clementl.metrotimex.utils.ofPattern
import java.time.LocalDate

class DayStatusListAdapter(val clickListener: DayListener) : ListAdapter<DayStatus, DayStatusListAdapter.DayStatusViewHolder> (DayStatusComparator()){

    companion object {
        val today = LocalDate.now()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).workDayType?.type ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayStatusViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SHIFT -> ShiftViewHolder(
                CellShiftBinding.inflate(layoutInflater, parent, false)
            )
            else -> NonShiftViewHolder(
                CellWeekendBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: DayStatusViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
    }

    abstract class DayStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(day: DayStatus, clickListener: DayListener) {
            if (day.date == today) {
                itemView.setBackgroundResource(R.color.light_blue_gray)
            }
            itemView.setOnClickListener {

            }
        }
    }

    class ShiftViewHolder(val binding: CellShiftBinding) : DayStatusViewHolder(binding.root) {

        override fun bind(day: DayStatus, clickListener: DayListener) {
            super.bind(day, clickListener)
            binding.clickListener = clickListener
            binding.day = day
            binding.executePendingBindings()
        }
    }

    class NonShiftViewHolder(val binding: CellWeekendBinding) : DayStatusViewHolder(binding.root) {

        override fun bind(day: DayStatus, clickListener: DayListener) {
            super.bind(day, clickListener)
            binding.clickListener = clickListener
            binding.day = day
            binding.executePendingBindings()
        }
    }

    class DayStatusComparator : DiffUtil.ItemCallback<DayStatus>() {
        override fun areItemsTheSame(oldItem: DayStatus, newItem: DayStatus): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DayStatus, newItem: DayStatus): Boolean {
            return oldItem == newItem
        }
    }
}

class DayListener(val clickListener: (dateId: Long) -> Unit) {
    fun onClick(day: DayStatus) = clickListener(day.dateLong)
}