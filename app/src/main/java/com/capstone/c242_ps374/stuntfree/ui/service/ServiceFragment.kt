package com.capstone.c242_ps374.stuntfree.ui.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.data.model.Doctor
import com.capstone.c242_ps374.stuntfree.data.model.Healthcare
import com.capstone.c242_ps374.stuntfree.databinding.FragmentServiceBinding
import com.capstone.c242_ps374.stuntfree.ui.ArticleViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.ArticleAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.DoctorAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.HealthcareAdapter
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogEmergencyCallFragment
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogRedirectMapsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticleViewModel by viewModels()

    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var healthcareAdapter: HealthcareAdapter

    val doctors = listOf(
        Doctor(
            name = "Dr. John Doe",
            specialization = "Cardiologist",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/5/5a/John_Doe%2C_born_John_Nommensen_Duchac.jpg",
            distance = "5 km",
            availableTime = "Mon-Fri: 9 AM - 5 PM"
        ),
        Doctor(
            name = "Dr. Sarah Williams",
            specialization = "Neurologist",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/16/Sarah_Anne_Williams_Animate_Miami_2015.jpg",
            distance = "10 km",
            availableTime = "Mon-Fri: 10 AM - 6 PM"
        ),
        Doctor(
            name = "Dr. Emily White",
            specialization = "Dermatologist",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/e/e9/CCP_March_14%2C_2012.jpg",
            distance = "7 km",
            availableTime = "Mon-Fri: 8 AM - 4 PM"
        ),
        Doctor(
            name = "Dr. Michael Brown",
            specialization = "Orthopedist",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/a/a6/BeBe_Winans_2019.jpg",
            distance = "3 km",
            availableTime = "Mon-Sat: 9 AM - 7 PM"
        ),
        Doctor(
            name = "Dr. Jessica Green",
            specialization = "Pediatrician",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/76/Cece_Bell_%282024%29.jpg",
            distance = "8 km",
            availableTime = "Mon-Fri: 9 AM - 5 PM"
        )
    )

    val healthcare = listOf(
        Healthcare(
            name = "Mount Sinai Hospital",
            specialization = "Merawat lansia",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/88/Hospital-de-Bellvitge.jpg",
            distance = "12 km",
            availableTime = "24/7"
        ),
        Healthcare(
            name = "Cleveland Clinic",
            specialization = "Merawat gen-z",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/76/Mateer_Building-_Penn_State_School_of_Hospitality_Management.JPG",
            distance = "15 km",
            availableTime = "24/7"
        ),
        Healthcare(
            name = "Mayo Clinic",
            specialization = "Rawat inap",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/10/StatlerHotel.jpg",
            distance = "20 km",
            availableTime = "24/7"
        ),
        Healthcare(
            name = "Johns Hopkins Hospital",
            specialization = "IGD",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c1/Ecole-hoteliere-de-Lausanne_-Lausanne-hospitality-management-school_campus-aerial-view_web.jpg",
            distance = "18 km",
            availableTime = "24/7"
        ),
        Healthcare(
            name = "Kaiser Permanente",
            specialization = "UGD",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/61/Rosen_UCF_%287981398564%29.jpg",
            distance = "25 km",
            availableTime = "24/7"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupRecyclerViews()
        setupSearchView()
    }

    private fun setupRecyclerViews() {
        binding.recyclerDokter.apply {
            adapter = doctorAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.recyclerHealth.apply {
            adapter = healthcareAdapter
            layoutManager = LinearLayoutManager(context)
        }

        doctorAdapter.submitList(doctors)
        healthcareAdapter.submitList(healthcare)
    }

    private fun setupAdapters() {
        doctorAdapter = DoctorAdapter(
            onCallClick = { doctor ->
                val dialog = CustomDialogEmergencyCallFragment().apply {
                    onYesClick = {
                        Toast.makeText(context, "Memanggil ${doctor.name}", Toast.LENGTH_SHORT).show()
                    }
                    onNoClick = {
                        Toast.makeText(context, "Aksi batal.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show(parentFragmentManager, "CustomDialog")
            },
            onLocationClick = { service ->
                val dialog = CustomDialogRedirectMapsFragment().apply {
                    onYesClick = {
                        Toast.makeText(context, "Menampilkan lokasi ${service.name}", Toast.LENGTH_SHORT).show()
                    }
                    onNoClick = {
                        Toast.makeText(context, "Aksi batal.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show(parentFragmentManager, "CustomDialog")
            }
        )

        // Initialize the HealthcareAdapter
        healthcareAdapter = HealthcareAdapter(
            onCallClick = { healthcare ->
                val dialog = CustomDialogEmergencyCallFragment().apply {
                    onYesClick = {
                        Toast.makeText(context, "Memanggil ${healthcare.name}", Toast.LENGTH_SHORT).show()
                    }
                    onNoClick = {
                        Toast.makeText(context, "Aksi batal.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show(parentFragmentManager, "CustomDialog")
            },
            onLocationClick = { service ->
                val dialog = CustomDialogRedirectMapsFragment().apply {
                    onYesClick = {
                        Toast.makeText(context, "Menampilkan lokasi ${service.name}", Toast.LENGTH_SHORT).show()
                    }
                    onNoClick = {
                        Toast.makeText(context, "Aksi batal.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show(parentFragmentManager, "CustomDialog")
            }
        )
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchArticles(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchArticles(it)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}