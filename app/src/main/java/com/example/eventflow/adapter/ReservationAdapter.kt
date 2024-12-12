package com.example.eventflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventflow.databinding.ReservationItemBinding
import com.example.eventflow.models.ReservationModel

class ReservationAdapter: RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    private val reservationList: MutableList<ReservationModel> = mutableListOf()

    private var filteredItems: MutableList<ReservationModel> = mutableListOf()

    fun submitList(list: List<ReservationModel>) {
        reservationList.clear()
        reservationList.addAll(list)

        filteredItems = ArrayList(reservationList)
        notifyDataSetChanged()
    }

    fun filterList(query: String) {
        filteredItems = if (query.isEmpty()) {
            ArrayList(reservationList)
        } else {
            reservationList.filter {
                it.reservationName.contains(query, ignoreCase = true) ||
                it.reservationCustomerName.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    class ReservationViewHolder(val binding: ReservationItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationAdapter.ReservationViewHolder {
        val binding = ReservationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReservationAdapter.ReservationViewHolder,
        position: Int
    ) {
        val reservation = filteredItems[position]
        holder.binding.reservation = reservation
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }
}