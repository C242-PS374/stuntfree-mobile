package com.capstone.c242_ps374.stuntfree.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.FragmentHomeBinding
import com.capstone.c242_ps374.stuntfree.ui.ArticleViewModel
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.HomeAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.WeekAdapter
import com.capstone.c242_ps374.stuntfree.ui.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.RecyclerStatusAdapter
import com.capstone.c242_ps374.stuntfree.ui.camera.CameraActivity
import com.capstone.c242_ps374.stuntfree.ui.custom.CustomDialogFragment
import com.capstone.c242_ps374.stuntfree.ui.stuntingreport.StuntingReportActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val serviceViewModel: ArticleViewModel by viewModels()
    private lateinit var weekAdapter: WeekAdapter
    private lateinit var homeAdapter: HomeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        setupWeekRecyclerView()
        observeWeekData()
        setupBinding()
        setupRecyclerStatus()

        serviceViewModel.getTodayDate()
        serviceViewModel.fetchNews()
        serviceViewModel.generateWeekDays()
    }

    private fun setupButton() {

        binding.logSymptoms.setOnClickListener {
            val intent = Intent(requireContext(), LogYourSymptomsActivity::class.java)
            startActivity(intent)
        }

        binding.cameraScan.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }

        binding.quickCall.setOnClickListener {
            val customDialogFragment = CustomDialogFragment()
            customDialogFragment.show(parentFragmentManager, CustomDialogFragment::class.java.simpleName)
        }

    }

    private fun setupBinding() {
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.ivCalendar.setOnClickListener {
            val intent = Intent(requireContext(), StuntingReportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupWeekRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvInsight.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
        }

        weekAdapter = WeekAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = weekAdapter
        }
    }

    private fun observeWeekData() {
        serviceViewModel.news.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val articles = response.articles ?: listOf()
                if (articles.isEmpty()) {
                    binding.rvInsight.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    homeAdapter.submitList(articles)
                    binding.rvInsight.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }.onFailure { exception ->
                binding.rvInsight.visibility = View.GONE
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        serviceViewModel.weekDays.observe(viewLifecycleOwner) { weekData ->
            weekAdapter.submitList(weekData)
        }

        serviceViewModel.todayDate.observe(viewLifecycleOwner) { todayDate ->
            binding.tvTodayDate.text = "$todayDate"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val statusList = listOf(
        RecyclerStatusAdapter.StatusItem(
            title = "Pregnancy - Day 4",
            stuntingDescription = "3 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Not Fulfilled",
            environmentalDescription = "Healthy Environment"
        ),
        RecyclerStatusAdapter.StatusItem(
            title = "Pregnancy - Day 5",
            stuntingDescription = "2 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Partially Fulfilled",
            environmentalDescription = "Moderate Environment"
        ),
        RecyclerStatusAdapter.StatusItem(
            title = "Pregnancy - Day 6",
            stuntingDescription = "1 Hari sebelum proses deteksi stunting",
            nutritionDescription = "Fulfilled",
            environmentalDescription = "Optimal Environment"
        )
    )


    private fun setupRecyclerStatus() {
        val recyclerStatusAdapter = RecyclerStatusAdapter(statusList)
        binding.recyclerStatus.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recyclerStatusAdapter
        }
    }
}

