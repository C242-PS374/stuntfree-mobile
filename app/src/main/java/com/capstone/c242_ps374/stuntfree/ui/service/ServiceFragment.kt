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
import com.capstone.c242_ps374.stuntfree.databinding.FragmentServiceBinding
import com.capstone.c242_ps374.stuntfree.ui.ServiceViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.ServiceAdapter
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var adapter: ServiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setupView()

        setupSearchView()

        observeData()

        fetchServices()
    }

    private fun setupView() {
        binding.recyclerDokter.apply {
            this.adapter = this@ServiceFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.recyclerHealth.apply {
            this.adapter = this@ServiceFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun setupAdapter() {
        adapter = ServiceAdapter(
            onCallClick = { service ->
                val dialog = CustomDialogFragment().apply {
                    onYesClick = {
                        Toast.makeText(context, "Memanggil ${service.name}", Toast.LENGTH_SHORT).show()
                    }
                    onNoClick = {
                        Toast.makeText(context, "Aksi batal.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show(parentFragmentManager, "CustomDialog")
            },
            onLocationClick = { service ->
                val dialog = CustomDialogFragment().apply {
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
                    viewModel.searchServices(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchServices(it)
                }
                return true
            }
        })
    }

    private fun observeData() {
        viewModel.services.observe(viewLifecycleOwner) { services ->
            if (!services.isNullOrEmpty()) {
                adapter.submitList(services)
                binding.recyclerHealth.visibility = View.VISIBLE
                binding.recyclerDokter.visibility = View.VISIBLE
                binding.textNoResults.visibility = View.GONE
            } else {
                binding.recyclerHealth.visibility = View.GONE
                binding.recyclerDokter.visibility = View.GONE
                binding.textNoResults.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.recyclerDokter.visibility = View.GONE
            binding.recyclerHealth.visibility = View.GONE
            binding.textNoResults.visibility = View.GONE
        }
    }

    private fun fetchServices() {
        viewModel.fetchServices()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}