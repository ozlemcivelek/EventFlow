package com.example.eventflow.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventflow.Event
import com.example.eventflow.databinding.RecyclerRowBinding

class EventAdapter :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val eventList: MutableList<Event> = mutableListOf()
    var onItemClicked: (Event) -> Unit = {}

    fun setItems(items: List<Event>) {
        this.eventList.clear()
        this.eventList.addAll(items)
        notifyDataSetChanged()
    }

    class EventViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.EventViewHolder {
        val binding = RecyclerRowBinding.inflate(
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
            firstDateTextView.text = event.firstTime
            lastDateTextView.text = event.lastTime
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}