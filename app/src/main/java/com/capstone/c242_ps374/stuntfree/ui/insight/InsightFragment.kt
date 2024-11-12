package com.capstone.c242_ps374.stuntfree.ui.insight

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.c242_ps374.stuntfree.R

class InsightFragment : Fragment() {

    companion object {
        fun newInstance() = InsightFragment()
    }

    private val viewModel: InsightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }
}