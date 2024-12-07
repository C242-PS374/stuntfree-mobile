package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.data.service.Service
import com.capstone.c242_ps374.stuntfree.databinding.ItemButtonBinding

class ButtonAdapter(
    private val onButtonClick: (Service) -> Unit
) : ListAdapter<Service, ButtonAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    private val selectedButton = MutableLiveData<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding, onButtonClick)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = getItem(position)
        holder.bind(service)
        holder.itemView.setOnClickListener {
            selectedButton.value = if (selectedButton.value == service.name) null else service.name
            onButtonClick(service)
        }
    }

    class ServiceViewHolder(
        private val binding: ItemButtonBinding,
        private val onButtonClick: (Service) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service?) {
            service?.let {
                binding.apply {
                    btnTitleAtas.text = it.name ?: "Unknown"
                    btnTitleAtas.setOnClickListener {
                        onButtonClick(service)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem == newItem
            }
        }
    }
}

