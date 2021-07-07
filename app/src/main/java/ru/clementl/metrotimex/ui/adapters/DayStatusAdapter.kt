package ru.clementl.metrotimex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.model.data.*
import ru.clementl.metrotimex.utils.asSimpleTime

abstract class DayStatusHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(day: DayStatus)
}

class DayStatusAdapter : RecyclerView.Adapter<DayStatusHolder>() {

    private val mDayList: MutableList<DayStatus> = mutableListOf()



    fun setData(newDays: List<DayStatus>) {
        mDayList.clear()
        mDayList.addAll(newDays)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return mDayList[position].workDayType.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayStatusHolder {
        return when (viewType) {
            TYPE_SHIFT -> ShiftViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_shift, parent, false)
            )
            else -> NonWorkdayViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_weekend, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return mDayList.count()
    }

    override fun onBindViewHolder(holder: DayStatusHolder, position: Int) {
        holder.bind(day = mDayList[position])
    }

    class ShiftViewHolder(itemView: View) : DayStatusHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.date)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        val tvShiftName: TextView = itemView.findViewById(R.id.shiftName)
        val tvStartTime: TextView = itemView.findViewById(R.id.startTime)
        val tvStartPlace: TextView = itemView.findViewById(R.id.startPlace)
        val tvEndTime: TextView = itemView.findViewById(R.id.main_text)
        val tvEndPlace: TextView = itemView.findViewById(R.id.endPlace)

        override fun bind(day: DayStatus) {

            tvDate.text = day.date.dayOfMonth.toString()
            tvDayOfWeek.text = day.date.dayOfWeek.toString()
            with(day.shift!!) {
                tvShiftName.text = name
                tvStartTime.text = startTime.asSimpleTime()
                tvStartPlace.text = startLoc
                tvEndTime.text = endTime.asSimpleTime()
                tvEndPlace.text = endLoc
            }
        }
    }

    class NonWorkdayViewHolder(itemView: View) : DayStatusHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.date)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        val tvMainText: TextView = itemView.findViewById(R.id.main_text)

        override fun bind(day: DayStatus) {
            tvDate.text = day.date.dayOfMonth.toString()
            tvDayOfWeek.text = day.date.dayOfWeek.toString()
            tvMainText.text = day.workDayType.desc
        }
    }
}