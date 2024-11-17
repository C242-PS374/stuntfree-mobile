package com.capstone.c242_ps374.stuntfree.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private lateinit var binding: FragmentOnBoardingBinding
    private val viewModel: OnBoardingViewModel by activityViewModels()

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int) = OnBoardingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, position)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0

        viewModel.onboardingItems.observe(viewLifecycleOwner) { items ->
            val currentItem = items[position]

            binding.imageIllustration.setImageResource(currentItem.image)
            binding.titleText.text = currentItem.title
            binding.descriptionText.text = currentItem.description

            // Tambahkan animasi pada teks
            binding.titleText.alpha = 0f
            binding.descriptionText.alpha = 0f

            binding.titleText.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(300)
                .start()

            binding.descriptionText.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(500)
                .start()
        }
    }
}