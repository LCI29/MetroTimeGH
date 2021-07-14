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
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.utils.asSimpleTime

class DayStatusListAdapter : ListAdapter<DayStatus, DayStatusListAdapter.DayStatusViewHolder> (DayStatusComparator()){

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
        abstract fun bind(day: DayStatus)
    }

    class ShiftViewHolder(itemView: View) : DayStatusViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.date)
        private val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        private val tvShiftName: TextView = itemView.findViewById(R.id.shiftName)
        private val tvStartTime: TextView = itemView.findViewById(R.id.startTime)
        private val tvStartPlace: TextView = itemView.findViewById(R.id.startPlace)
        private val tvEndTime: TextView = itemView.findViewById(R.id.main_text)
        private val tvEndPlace: TextView = itemView.findViewById(R.id.endPlace)

        override fun bind(day: DayStatus) {
            tvDate.text = day.date?.dayOfMonth.toString()
            tvDayOfWeek.text = day.date?.dayOfWeek.toString()
            with(day.shift!!) {
                tvShiftName.text = name
                tvStartTime.text = startTime?.asSimpleTime()
                tvStartPlace.text = startLoc
                tvEndTime.text = endTime?.asSimpleTime()
                tvEndPlace.text = endLoc
            }
        }
    }

    class NonShiftViewHolder(itemView: View) : DayStatusViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.date)
        private val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        private val tvMainText: TextView = itemView.findViewById(R.id.main_text)

        override fun bind(day: DayStatus) {
            tvDate.text = day.date?.dayOfMonth.toString()
            tvDayOfWeek.text = day.date?.dayOfWeek.toString()
            tvMainText.text = day.workDayType?.desc
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