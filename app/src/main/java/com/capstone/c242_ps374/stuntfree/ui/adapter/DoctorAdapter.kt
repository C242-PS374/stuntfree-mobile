package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.model.Doctor
import com.capstone.c242_ps374.stuntfree.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val onCallClick: (Doctor) -> Unit,
    private val onLocationClick: (Doctor) -> Unit
) : ListAdapter<Doctor, DoctorAdapter.DoctorViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding, onCallClick, onLocationClick)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = getItem(position)
        doctor?.let { holder.bind(it) }
    }

    class DoctorViewHolder(
        private val binding: ItemDoctorBinding,
        private val onCallClick: (Doctor) -> Unit,
        private val onLocationClick: (Doctor) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(doctor: Doctor) {
            binding.apply {
                textName.text = doctor.name ?: "Unknown Doctor"
                textSpesialis.text = doctor.specialization ?: "No Specialty Available"
                textDistance.text = doctor.distance ?: "No Distance Available"
                textTime.text = doctor.availableTime ?: "No Time Available"

                Glide.with(itemView.context)
                    .load(doctor.imageUrl)
                    .into(imageDoctor)

                imageCall.setOnClickListener { onCallClick(doctor) }
                imageLocation.setOnClickListener { onLocationClick(doctor) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
                return oldItem == newItem
            }
        }
    }
}
