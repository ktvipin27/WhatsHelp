package com.whatshelp.ui.history

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whatshelp.R
import com.whatshelp.data.db.entity.History
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.BaseFragment
import com.whatshelp.util.Constants.EXTRA_NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<HistoryViewModel>() {

    override val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            HistoryScreen { history ->
                onClickHistory(history)
            }
        }
    }

    private fun onClickHistory(history: History) {
        findNavController().let { navController ->
            analyticsManager.logEvent(AnalyticsEvent.History.Click)
            navController.previousBackStackEntry?.savedStateHandle?.set(
                EXTRA_NUMBER,
                history.whatsAppNumber.fullNumber
            )
            navController.navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.history.observe(viewLifecycleOwner, { history ->
            setHasOptionsMenu(history.isNotEmpty())
        })

        /*ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteHistory(viewHolder.bindingAdapterPosition)
                analyticsManager.logEvent(AnalyticsEvent.History.Delete)
            }
        }).attachToRecyclerView(binding.rvHistory)*/
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