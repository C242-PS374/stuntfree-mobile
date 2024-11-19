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
                title = "Selamat Datang",
                description = "Mulai perjalanan belajar Anda dengan cara yang menyenangkan"
            ),
            OnBoardingItem(
                image = R.drawable.illustration_2,
                title = "Belajar Mudah",
                description = "Materi yang disusun secara sistematis dan menarik"
            ),
            OnBoardingItem(
                image = R.drawable.illustration_3,
                title = "Tantang Diri",
                description = "Kuis interaktif untuk mengukur pemahaman Anda"
            )
        )
        _onboardingItems.value = items
    }
}