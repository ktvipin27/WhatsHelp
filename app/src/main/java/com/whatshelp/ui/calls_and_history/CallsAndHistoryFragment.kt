package com.whatshelp.ui.calls_and_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_LABELED
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
import com.google.android.material.tabs.TabLayoutMediator
import com.whatshelp.R
import com.whatshelp.databinding.FragmentCallsAndHistoryBinding
import com.whatshelp.ui.base.VBFragment
import com.whatshelp.ui.calllogs.CallLogsFragment
import com.whatshelp.ui.history.HistoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallsAndHistoryFragment :
    VBFragment<FragmentCallsAndHistoryBinding, CallsAndHistoryViewModel>() {

    override val viewModel: CallsAndHistoryViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCallsAndHistoryBinding =
        FragmentCallsAndHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewPager.adapter = ScreenSlidePagerAdapter()
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.title_call_logs)
                    else -> getString(R.string.title_history)
                }
                tab.icon = when (position) {
                    0 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_call_log)
                    else -> ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_baseline_history_24)
                }
            }.attach()
            tabLayout.run {
                getTabAt(0)?.tabLabelVisibility = TAB_LABEL_VISIBILITY_LABELED
                getTabAt(1)?.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.tabLabelVisibility = TAB_LABEL_VISIBILITY_LABELED
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        tab?.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {}

                })
            }
        }
    }

    inner class ScreenSlidePagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> CallLogsFragment()
            else -> HistoryFragment()
        }
    }
}