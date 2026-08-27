package com.capstone.c242_ps374.stuntfree.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.c242_ps374.stuntfree.R

class OnBoardingViewModel : ViewModel() {
    private val _onboardingItems = MutableLiveData<List<OnBoardingItem>>()
    val onboardingItems: LiveData<List<OnBoardingItem>> = _onboardingItems

    init {
        loadOnboardingItems()
    }

    private fun loadOnboardingItems() {
        val items = listOf(
            OnBoardingItem(
                image = R.drawable.illustration_1,
                title = "Early Detection for Bright Futures",
                description = "Identifying and addressing stunting early to protect children’s development."
            ),
            OnBoardingItem(
                image = R.drawable.illustration_2,
                title = "Support Mothers for Lasting Health",
                description = "Guidance and resources for maternal health and early childhood care."
            ),
            OnBoardingItem(
                image = R.drawable.illustration_3,
                title = "Healthy Growth, Happy Future",
                description = "Ensuring every child grows up healthy and full of potential."
            )
        )
        _onboardingItems.value = items
    }
}