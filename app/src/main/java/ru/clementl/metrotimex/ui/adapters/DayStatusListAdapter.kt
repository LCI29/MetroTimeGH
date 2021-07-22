package ru.clementl.metrotimex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TYPE_SHIFT
import ru.clementl.metrotimex.utils.asSimpleTime
import ru.clementl.metrotimex.utils.ofPattern
import java.time.LocalDate

class DayStatusListAdapter : ListAdapter<DayStatus, DayStatusListAdapter.DayStatusViewHolder> (DayStatusComparator()){

    companion object {
        val today = LocalDate.now()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).workDayType?.type ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayStatusViewHolder {
        return when (viewType) {
            TYPE_SHIFT -> ShiftViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_shift, parent, false)
            )
            else -> NonShiftViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_weekend, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: DayStatusViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    abstract class DayStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(day: DayStatus) {
            if (day.date == today) {
                itemView.setBackgroundResource(android.R.color.holo_orange_light)
            }
            itemView.setOnClickListener {

            }
        }
    }

    class ShiftViewHolder(itemView: View) : DayStatusViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.date)
        private val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        private val tvShiftName: TextView = itemView.findViewById(R.id.shiftName)
        private val tvShiftDescString: TextView = itemView.findViewById(R.id.shift_desc_string)

        override fun bind(day: DayStatus) {
            super.bind(day)
            tvDate.text = day.date?.dayOfMonth.toString()
            tvDayOfWeek.text = day.date?.ofPattern("EE")
            with(day.shift!!) {
                tvShiftName.text = if (name?.isEmpty() != false) "Смена" else name
                tvShiftDescString.text = day.shift.getDescriptionString()

            }
        }
    }

    class NonShiftViewHolder(itemView: View) : DayStatusViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.date)
        private val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        private val tvShiftName: TextView = itemView.findViewById(R.id.shiftName)

        override fun bind(day: DayStatus) {
            super.bind(day)
            tvDate.text = day.date?.dayOfMonth.toString()
            tvDayOfWeek.text = day.date?.ofPattern("EE")
            tvShiftName.text = day.workDayType?.desc
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

class DayListener(val clickListener: (date: LocalDate) -> Unit) {
    fun onClick(day: DayStatus) = clickListener(day.date!!)
}