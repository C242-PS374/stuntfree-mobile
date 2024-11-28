package com.capstone.c242_ps374.stuntfree.ui.insight

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.FragmentInsightBinding
import com.capstone.c242_ps374.stuntfree.databinding.FragmentServiceBinding
import com.capstone.c242_ps374.stuntfree.ui.ServiceViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.ButtonAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.InsightAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.ServiceAdapter
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightFragment : Fragment() {

    private var _binding: FragmentInsightBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var adapter: InsightAdapter
    private lateinit var adapterButton: ButtonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        // Mulai memuat data dari ViewModel
        viewModel.fetchServicesWithButton()
    }

    private fun setupRecyclerView() {
        // RecyclerView untuk Button
        adapterButton = ButtonAdapter { service ->
            viewModel.filterServicesByButton(service.name ?: "Unknown")
        }

        binding.recyclerButton.apply {
            adapter = adapterButton
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        // RecyclerView untuk Insight
        adapter = InsightAdapter()
        binding.recyclerInsight.apply {
            adapter = this@InsightFragment.adapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchServices(it.trim())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchServices(it.trim())
                }
                return true
            }
        })
    }

    private fun observeViewModel() {
        // Mengamati LiveData untuk RecyclerView Insight
        viewModel.services.observe(viewLifecycleOwner) { services ->
            setLoading(false)
            if (!services.isNullOrEmpty()) {
                adapter.submitList(services)
                binding.recyclerInsight.visibility = View.VISIBLE
                binding.textNoResults.visibility = View.GONE
            } else {
                binding.recyclerInsight.visibility = View.GONE
                binding.textNoResults.visibility = View.VISIBLE
            }
        }

        // Mengamati LiveData untuk RecyclerView Button
        viewModel.buttonServices.observe(viewLifecycleOwner) { buttonServices ->
            if (!buttonServices.isNullOrEmpty()) {
                adapterButton.submitList(buttonServices)
                binding.recyclerButton.visibility = View.VISIBLE
            } else {
                binding.recyclerButton.visibility = View.GONE
            }
        }

        // Mengamati error jika ada
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            setLoading(false)
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Mengamati status loading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.recyclerInsight.visibility = View.GONE
            binding.recyclerButton.visibility = View.GONE
            binding.textNoResults.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

