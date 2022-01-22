package ru.clementl.metrotimex.screens.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.databinding.CellShiftBinding
import ru.clementl.metrotimex.databinding.CellWeekendBinding
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TYPE_SHIFT
import ru.clementl.metrotimex.utils.fullDate
import java.time.DayOfWeek
import java.time.LocalDate

class DayStatusListAdapter(private val clickListener: DayListener) :
    ListAdapter<DayStatus, DayStatusListAdapter.DayStatusViewHolder>(DayStatusComparator()) {


    companion object {
        val today: LocalDate
            get() = LocalDate.now()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).workDayType?.type ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayStatusViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SHIFT -> ShiftViewHolder(
                CellShiftBinding.inflate(layoutInflater, parent, false),
                parent.context
            )
            else -> NonShiftViewHolder(
                CellWeekendBinding.inflate(layoutInflater, parent, false),
                parent.context
            )
        }
    }

    override fun onBindViewHolder(holder: DayStatusViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
    }

    abstract class DayStatusViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        open fun bind(day: DayStatus, clickListener: DayListener) {
            when {
                day.date == today -> itemView.setBackgroundResource(R.color.today_calendar_color)
                else -> itemView.setBackgroundResource(R.color.transparent)
            }


//            setOnLongClick(day)
        }

        /**
         * Вариант реализации контекстного меню через диалог. Чтобы активировать, нужно
         * просто раскомментировать вызов setOnLongClick в DayStatusViewHolder.bind()
         */
        private fun setOnLongClick(day: DayStatus) {
            itemView.setOnLongClickListener { view ->
                val items = arrayOf("Редактировать", "Удалить")

                val builder = AlertDialog.Builder(context)

                builder.setTitle(
                    """
                            ${day.date.fullDate(true)}
                            ${day.shift?.getDescriptionString(true) ?: day.workDayType?.desc}
                        """.trimIndent()
                )
                    .setItems(items) { _, pos ->
                        TODO("Make Edit and Delete options")
                    }
                builder.show()
                true
            }
        }


    }

    class ShiftViewHolder(val binding: CellShiftBinding, context: Context) :
        DayStatusViewHolder(binding.root, context) {

        override fun bind(day: DayStatus, clickListener: DayListener) {
            super.bind(day, clickListener)
            binding.clickListener = clickListener
            binding.day = day
            binding.executePendingBindings()

            // Showing or hiding weekly divider
            if (day.date.dayOfWeek == DayOfWeek.SUNDAY) {
                binding.weekDivider.visibility = View.VISIBLE
            } else {
                binding.weekDivider.visibility = View.GONE
            }


        }
    }

    class NonShiftViewHolder(val binding: CellWeekendBinding, context: Context) :
        DayStatusViewHolder(binding.root, context) {

        override fun bind(day: DayStatus, clickListener: DayListener) {
            super.bind(day, clickListener)
            binding.clickListener = clickListener
            binding.day = day
            binding.executePendingBindings()

            // Showing or hiding weekly divider
            if (day.date.dayOfWeek == DayOfWeek.SUNDAY) {
                binding.weekDivider.visibility = View.VISIBLE
            } else {
                binding.weekDivider.visibility = View.GONE
            }


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