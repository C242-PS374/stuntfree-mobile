package com.capstone.c242_ps374.stuntfree.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.databinding.FragmentProfileBinding
import com.capstone.c242_ps374.stuntfree.ui.AttachViewModel
import com.capstone.c242_ps374.stuntfree.ui.AuthViewModel
import com.capstone.c242_ps374.stuntfree.ui.custom.PopUpLogout
import com.capstone.c242_ps374.stuntfree.ui.stuntingreport.StuntingReportActivity
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val attachViewModel: AttachViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModelData()

        lifecycleScope.launch {
            sessionManager.getEmail().collect { email ->
                if (email != null) {
                    val stage = sessionManager.getStage(email).first()
                    if (stage == "pregnancy") {
                        binding.tvStatus.text = "$stage"
                    } else {
                        binding.tvStatus.text = "$stage"
                    }
                }
            }
        }

        attachViewModel.getUserProfile()
        attachViewModel.predictStunting()
    }

    private fun setupUI() {
        binding.tvEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.actionLogout.setOnClickListener {
            val customDialogFragment = PopUpLogout()
            customDialogFragment.show(parentFragmentManager, PopUpLogout::class.java.simpleName)
        }

        binding.linearLayout.findViewById<LinearLayout>(R.id.stunting_report).setOnClickListener {
            val intent = Intent(requireContext(), StuntingReportActivity::class.java)
            startActivity(intent)
        }

        binding.language.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModelData() {
        attachViewModel.userProfileResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = resource.data
                    Log.d("ProfileFragment", "${profile?.data}")
                    if (profile?.data != null) {
                        binding.tvName.text = "Name: ${profile.data.profile?.name ?: "N/A"}"
                        binding.tvEmail.text = "Email: ${profile.data.email ?: "N/A"}"
                    } else {
                        binding.tvName.text = "Name: N/A"
                        binding.tvEmail.text = "Email: N/A"
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        attachViewModel.predictStuntingResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = resource.data
                    Log.d("ProfileFragment", "${profile?.result}")
                    if (profile?.result != null) {
                        binding.tvStunting.text = "${profile.result}"
                    } else {
                        binding.tvStunting.text = "N/A"
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        authViewModel.authState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showError(resource.message)
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

    private fun showError(message: String?) {
        Toast.makeText(requireContext(), "Error: ${message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}