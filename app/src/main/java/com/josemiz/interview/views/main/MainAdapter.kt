package com.josemiz.interview.views.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.josemiz.interview.R
import com.josemiz.interview.databinding.ItemShiftBinding
import com.josemiz.interview.models.ColorEnum
import com.josemiz.interview.models.ShiftModel
import com.josemiz.interview.utils.convertDateToHourRange
import com.josemiz.interview.utils.convertJsonToDateString

class MainAdapter(
    private var list: List<ShiftModel>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ItemShiftBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_shift,
            parent,
            false
        ).let(::ViewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun loadList(list: List<ShiftModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemShiftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shift: ShiftModel) = with(binding) {
            tvName.text = "${shift.name}(${shift.role})"
            tvDate.text = "${convertJsonToDateString(shift.start_date)} ${
                convertDateToHourRange(
                    shift.start_date,
                    shift.end_date
                )
            }"
            setColor(vwColor, shift.color)
        }

        private fun setColor(view: View, color: String) {
            view.setBackgroundColor(ColorEnum.getColor(color)?.getColor() ?: Color.WHITE)
        }

    }
}