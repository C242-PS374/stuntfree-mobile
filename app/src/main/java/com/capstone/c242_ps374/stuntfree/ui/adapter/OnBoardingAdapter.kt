package com.capstone.c242_ps374.stuntfree.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.c242_ps374.stuntfree.ui.fragment.OnBoardingFragment

class OnBoardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return OnBoardingFragment.newInstance(position)
    }
}