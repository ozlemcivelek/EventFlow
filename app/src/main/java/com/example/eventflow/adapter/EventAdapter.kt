package com.example.eventflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventflow.models.EventModel
import com.example.eventflow.databinding.EventItemBinding

class EventAdapter :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val eventList: MutableList<EventModel> = mutableListOf()
    var onItemClicked: (EventModel) -> Unit = {}

    fun setItems(items: List<EventModel>) {
        this.eventList.clear()
        this.eventList.addAll(items)
        notifyDataSetChanged()
    }

    class EventViewHolder(val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.EventViewHolder {
        val binding = EventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EventAdapter.EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(event)
        }
        with(holder.binding) {
            eventTitle.text = event.title
            eventDescription.text = event.description
            dateTextView.text = event.prettyDate
            firstDateTextView.text = event.startTime
            lastDateTextView.text = event.endTime
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}