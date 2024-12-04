package com.capstone.c242_ps374.stuntfree.ui.profile

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.service.StuntingStatistics
import com.capstone.c242_ps374.stuntfree.databinding.FragmentProfileBinding
import com.capstone.c242_ps374.stuntfree.ui.auth.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: AuthViewModel by viewModels()

    private val stuntingData = listOf(
        StuntingStatistics("Ibu Hamil Berisiko Stunting", "Jumlah: 2500"),
        StuntingStatistics("Anak Berisiko Stunting", "Jumlah: 1800"),
        StuntingStatistics("Tindakan Penanggulangan Stunting", "Jumlah: 1200")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
        observeData()

        profileViewModel.fetchProfile()
    }


    private fun setupButton() {
        binding.tvEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.actionLogout.setOnClickListener {
            profileViewModel.logout()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun observeData() {
        // Observasi data profil
        profileViewModel.profileData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = resource.data?.profileResult
                    binding.tvName.text = "Name: ${profile?.name}"
                    binding.tvEmail.text = "Email: ${profile?.email}"
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Failed to load profile: ${resource.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        profileViewModel.authState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Logout failed: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
