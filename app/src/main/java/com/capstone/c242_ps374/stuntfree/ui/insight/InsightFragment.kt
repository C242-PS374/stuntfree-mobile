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
import com.capstone.c242_ps374.stuntfree.databinding.FragmentInsightBinding
import com.capstone.c242_ps374.stuntfree.ui.ArticleViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.ButtonAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.InsightAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightFragment : Fragment() {

    private var _binding: FragmentInsightBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticleViewModel by viewModels()
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

        viewModel.fetchNews()
        viewModel.generateWeekDays()
    }

    private fun setupRecyclerView() {
        adapterButton = ButtonAdapter { service ->
            viewModel.filterNewsByAuthor(service.author ?: "Unknown")
        }

        binding.recyclerButton.apply {
            adapter = adapterButton
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        // Set up InsightAdapter
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
                query?.let { viewModel.searchArticles(it.trim()) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchArticles(it.trim()) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.news.observe(viewLifecycleOwner) { result ->
            setLoading(false)
            result.onSuccess { response ->
                val articles = response.articles ?: listOf()
                if (articles.isEmpty()) {
                    binding.recyclerInsight.visibility = View.GONE
                    binding.recyclerButton.visibility = View.GONE
                    binding.textNoResults.visibility = View.VISIBLE
                } else {
                    adapter.submitList(articles)
                    binding.recyclerInsight.visibility = View.VISIBLE
                    binding.recyclerButton.visibility = View.VISIBLE
                    binding.textNoResults.visibility = View.GONE
                }
            }.onFailure { exception ->
                binding.recyclerInsight.visibility = View.GONE
                binding.recyclerButton.visibility = View.GONE
                binding.textNoResults.visibility = View.VISIBLE
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            setLoading(false)
            errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.buttonServices.observe(viewLifecycleOwner) { buttonServices ->
            if (buttonServices.isEmpty()) {
                binding.recyclerButton.visibility = View.GONE
            } else {
                adapterButton.submitList(buttonServices)
                binding.recyclerButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerInsight.visibility = if (isLoading) View.GONE else View.VISIBLE
            recyclerButton.visibility = if (isLoading) View.GONE else View.VISIBLE
            textNoResults.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}