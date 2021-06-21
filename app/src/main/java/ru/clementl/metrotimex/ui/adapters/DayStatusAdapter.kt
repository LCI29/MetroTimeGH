package ru.clementl.metrotimex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.model.data.*

abstract class DayStatusHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(model: DayStatus)
}

class DayStatusAdapter : RecyclerView.Adapter<DayStatusHolder>() {

    private val mDayList: MutableList<DayStatus> = mutableListOf()



    fun setData(newDays: List<DayStatus>) {
        mDayList.clear()
        mDayList.addAll(newDays)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return mDayList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayStatusHolder {
        return when (viewType) {
            TYPE_SHIFT -> DayStatusAdapter.ShiftViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_shift, parent, false)
            )
            TYPE_WEEKEND -> DayStatusAdapter.WeekendViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_weekend, parent, false)
            )
            else -> DayStatusAdapter.ShiftViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_shift, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return mDayList.count()
    }

    override fun onBindViewHolder(holder: DayStatusHolder, position: Int) {
        holder.bind(model = mDayList[position])
    }

    class ShiftViewHolder(itemView: View) : DayStatusHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.date)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        val tvShiftName: TextView = itemView.findViewById(R.id.shiftName)
        val tvStartTime: TextView = itemView.findViewById(R.id.startTime)
        val tvStartPlace: TextView = itemView.findViewById(R.id.startPlace)
        val tvEndTime: TextView = itemView.findViewById(R.id.weekend_text)
        val tvEndPlace: TextView = itemView.findViewById(R.id.endPlace)

        override fun bind(model: DayStatus) {
            model as Shift
            tvDate.text = model.date.dayOfMonth.toString()
            tvDayOfWeek.text = model.date.dayOfWeek.toString()
            tvShiftName.text = model.shiftName
            tvStartTime.text = model.startTime.toString()
            tvStartPlace.text = model.startPlace
            tvEndTime.text = model.endTime.toString()
            tvEndPlace.text = model.endPlace
        }
    }

    class WeekendViewHolder(itemView: View) : DayStatusHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.date)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeek)

        override fun bind(model: DayStatus) {
            model as Weekend
            tvDate.text = model.date.dayOfMonth.toString()
            tvDayOfWeek.text = model.date.dayOfWeek.toString()

        }
    }
}