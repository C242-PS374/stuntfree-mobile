package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.model.Healthcare
import com.capstone.c242_ps374.stuntfree.databinding.ItemHealtcareBinding

class HealthcareAdapter(
    private val onCallClick: (Healthcare) -> Unit,
    private val onLocationClick: (Healthcare) -> Unit
) : ListAdapter<Healthcare, HealthcareAdapter.HealthcareViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthcareViewHolder {
        val binding = ItemHealtcareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthcareViewHolder(binding, onCallClick, onLocationClick)
    }

    override fun onBindViewHolder(holder: HealthcareViewHolder, position: Int) {
        val healthcare = getItem(position)
        healthcare?.let { holder.bind(it) }
    }

    class HealthcareViewHolder(
        private val binding: ItemHealtcareBinding,
        private val onCallClick: (Healthcare) -> Unit,
        private val onLocationClick: (Healthcare) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(healthcare: Healthcare) {
            binding.apply {
                textName.text = healthcare.name ?: "Unknown Healthcare"
                textSpesialis.text = healthcare.specialization ?: "No Specialty Available"
                textDistance.text = healthcare.distance ?: "No Distance Available"
                textTime.text = healthcare.availableTime ?: "No Time Available"

                Glide.with(itemView.context)
                    .load(healthcare.imageUrl)
                    .into(imageHealtcare)

                imageCall.setOnClickListener { onCallClick(healthcare) }
                imageLocation.setOnClickListener { onLocationClick(healthcare) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Healthcare>() {
            override fun areItemsTheSame(oldItem: Healthcare, newItem: Healthcare): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Healthcare, newItem: Healthcare): Boolean {
                return oldItem == newItem
            }
        }
    }
}
