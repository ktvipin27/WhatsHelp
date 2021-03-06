package com.whatshelp.ui.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.R
import com.whatshelp.databinding.FragmentHistoryBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.Constants.EXTRA_NUMBER
import com.whatshelp.util.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : DBFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override val viewModel: HistoryViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_history

    @Inject
    lateinit var historyAdapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter.setItemClickListener { history ->
            findNavController().let { navController ->
                analyticsManager.logEvent(AnalyticsEvent.History.Click)
                navController.previousBackStackEntry?.savedStateHandle?.set(EXTRA_NUMBER,
                    history.whatsAppNumber.fullNumber)
                navController.navigateUp()
            }
        }

        binding.rvHistory.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = historyAdapter
        }

        viewModel.history.observe(viewLifecycleOwner, { history ->
            historyAdapter.submitList(history)
            setHasOptionsMenu(history.isNotEmpty())
        })

        ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteHistory(viewHolder.bindingAdapterPosition)
                analyticsManager.logEvent(AnalyticsEvent.History.Delete)
            }
        }).attachToRecyclerView(binding.rvHistory)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.clearHistory()
                analyticsManager.logEvent(AnalyticsEvent.History.Clear)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}