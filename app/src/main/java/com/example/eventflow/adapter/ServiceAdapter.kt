package com.example.eventflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventflow.databinding.ServiceItemBinding
import com.example.eventflow.models.ServiceModel

class ServiceAdapter: RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    val serviceList: MutableList<ServiceModel> = mutableListOf()
    var onItemClicked: (ServiceModel) -> Unit = {}

    var onDeleteClicked: (ServiceModel) -> Unit = {}

    var onEditClicked: (ServiceModel) -> Unit = {}

    fun removeItem(item: ServiceModel) {
        val index = serviceList.indexOf(item)
        if (index != -1) {
            serviceList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun setItems(items: List<ServiceModel>) {
        this.serviceList.clear()
        this.serviceList.addAll(items)
        notifyDataSetChanged()
    }

    class ServiceViewHolder(val binding: ServiceItemBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceAdapter.ServiceViewHolder {
        val binding = ServiceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceAdapter.ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(service)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClicked(service)
        }
        holder.binding.editButton.setOnClickListener {
            onEditClicked(service)
        }
        holder.binding.service = service
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}