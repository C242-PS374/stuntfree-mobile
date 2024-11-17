@file:Suppress("DEPRECATION")

package com.capstone.c242_ps374.stuntfree

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.c242_ps374.stuntfree.databinding.ActivityGetStartedBinding
import com.capstone.c242_ps374.stuntfree.ui.adapter.CustomPageTransformer
import com.capstone.c242_ps374.stuntfree.ui.adapter.OnBoardingAdapter
import com.capstone.c242_ps374.stuntfree.ui.auth.login.LoginActivity
import com.capstone.c242_ps374.stuntfree.ui.fragment.OnBoardingViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    private val onboardingViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnBoardingAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(CustomPageTransformer())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = ""
            tab.customView = adapter.createFragment(position).view
            tab.view.isClickable = false
        }.attach()

        onboardingViewModel.onboardingItems.observe(this) { items ->
            binding.nextButton.text = if (items.size - 1 == binding.viewPager.currentItem)
                "Get Started" else "Next"
        }

        binding.nextButton.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            val itemCount = adapter.itemCount

            if (currentItem < itemCount - 1) {
                binding.viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                navigateToMainActivity()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })
    }

    private fun updateIndicators(position: Int) {
        val indicators = listOf(binding.indicator1, binding.indicator2, binding.indicator3)
        indicators.forEachIndexed { index, view ->
            view.isSelected = index == position
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
}