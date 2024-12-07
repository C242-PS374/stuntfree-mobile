package com.capstone.c242_ps374.stuntfree.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.FragmentHomeBinding
import com.capstone.c242_ps374.stuntfree.ui.ServiceViewModel
import com.capstone.c242_ps374.stuntfree.ui.adapter.HomeAdapter
import com.capstone.c242_ps374.stuntfree.ui.adapter.WeekAdapter
import com.capstone.c242_ps374.stuntfree.ui.auth.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val serviceViewModel: ServiceViewModel by viewModels()
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

        serviceViewModel.fetchServices()
        serviceViewModel.generateDummyData()
    }

    private fun setupButton() {

        binding.btnEditNewbornData.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Newborn Data clicked!", Toast.LENGTH_SHORT).show()
        }

        binding.logSymptoms.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }

        binding.cameraScan.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }

        binding.quickCall.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_serviceFragment)
        }

    }

    private fun setupBinding() {
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.ivCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
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
        serviceViewModel.services.observe(viewLifecycleOwner) { services ->
            homeAdapter.submitList(services)
        }

        serviceViewModel.weekDays.observe(viewLifecycleOwner) { weekData ->
            weekAdapter.submitList(weekData)
        }

        serviceViewModel.todayDate.observe(viewLifecycleOwner) { todayDate ->
            binding.tvTodayDate.text = "Today, $todayDate"
        }

        serviceViewModel.childAge.observe(viewLifecycleOwner) { childAge ->
            binding.tvChildAgeTitle.text = "Child Age:"
            binding.tvDayCount.text = childAge
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

