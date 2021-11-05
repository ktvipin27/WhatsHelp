package com.whatshelp.ui.historycontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.whatshelp.R
import com.whatshelp.databinding.FragmentHistoryContainerBinding
import com.whatshelp.ui.base.VBFragment
import com.whatshelp.ui.calllogs.CallLogsFragment
import com.whatshelp.ui.history.HistoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryContainerFragment :
    VBFragment<FragmentHistoryContainerBinding, HistoryContainerViewModel>() {

    override val viewModel: HistoryContainerViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHistoryContainerBinding =
        FragmentHistoryContainerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewPager.adapter = ScreenSlidePagerAdapter()
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.title_call_logs)
                    else -> getString(R.string.title_history)
                }
            }.attach()
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