package com.example.eventflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventflow.databinding.CustomerItemBinding
import com.example.eventflow.models.CustomerModel

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    val customerList: MutableList<CustomerModel> = mutableListOf()
    var onItemClicked: (CustomerModel) -> Unit = { }

    fun setItems(items: List<CustomerModel>) {
        this.customerList.clear()
        this.customerList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerViewHolder {
        val binding = CustomerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CustomerViewHolder,
        position: Int
    ) {
        val customer = customerList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(customer)
        }
        holder.binding.customer = customer
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    class CustomerViewHolder(val binding: CustomerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
