package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.news.Article
import com.capstone.c242_ps374.stuntfree.databinding.ItemServiceBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ServiceAdapter(
    private val onCallClick: (Service) -> Unit,
    private val onLocationClick: (Service) -> Unit
) : ListAdapter<Service, ServiceAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding, onCallClick, onLocationClick)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = getItem(position)
        if (service != null) {
            holder.bind(service)
        }
    }

    class ServiceViewHolder(
        private val binding: ItemServiceBinding,
        private val onCallClick: (Article) -> Unit,
        private val onLocationClick: (Article) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(Article: Article) {
            binding.apply {
                // Bind data ke View
                textName.text = service.name ?: "Unknown"
                textTime.text = "Today, ${formatDate(service.createdAt)} WIB"
                Glide.with(itemView.context)
                    .load(service.photoUrl)
//                    .placeholder(R.drawable.placeholder_image)
//                    .error(R.drawable.error_image)
                    .into(imageEvent)

                // Tambahkan aksi klik pada ikon
                imageCall.setOnClickListener { onCallClick(service) }
                imageLocation.setOnClickListener { onLocationClick(service) }
            }
        }

        private fun formatDate(timestamp: String?): String {
            return if (!timestamp.isNullOrEmpty()) {
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = dateFormat.parse(timestamp)
                    val formattedDate = SimpleDateFormat("HH:mm a", Locale.getDefault())
                    formattedDate.format(date)
                } catch (e: Exception) {
                    "Invalid Date"
                }
            } else {
                "Unknown Time"
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
