package com.whatshelp.ui.messages

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.R
import com.whatshelp.databinding.FragmentMessagesBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.Constants.EXTRA_ADD_MESSAGE
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import com.whatshelp.util.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : DBFragment<FragmentMessagesBinding, MessagesViewModel>() {

    override val viewModel: MessagesViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_messages

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

    private var currentSavedStateHandle: SavedStateHandle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        currentSavedStateHandle = navController.currentBackStackEntry?.savedStateHandle

        messagesAdapter.setItemClickListener { message ->
            analyticsManager.logEvent(AnalyticsEvent.QuickMessage.Click)
            with(navController) {
                previousBackStackEntry?.savedStateHandle?.set(EXTRA_MESSAGE, message.text)
                navigateUp()
            }
        }

        ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                analyticsManager.logEvent(AnalyticsEvent.QuickMessage.Delete)
                viewModel.deleteMessage(viewHolder.bindingAdapterPosition)
            }
        }).attachToRecyclerView(binding.rvMessages)

        binding.run {
            rvMessages.apply {
                addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
                adapter = messagesAdapter
            }

            fab.setOnClickListener {
                analyticsManager.logEvent(AnalyticsEvent.QuickMessage.New)
                navController.navigate(R.id.action_messagesFragment_to_addMessagesFragment)
            }

            fab.postDelayed({fab.show()},300)
        }

        viewModel.messages.observe(viewLifecycleOwner, { messages ->
            messagesAdapter.submitList(messages)
            setHasOptionsMenu(messages.isNotEmpty())
        })

        currentSavedStateHandle
            ?.getLiveData<Boolean>(EXTRA_ADD_MESSAGE)
            ?.observe( viewLifecycleOwner ) { isNewMessageAdded ->
                if (isNewMessageAdded)
                    binding.rvMessages.smoothScrollToPosition(messagesAdapter.itemCount)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_quick_messages, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                analyticsManager.logEvent(AnalyticsEvent.QuickMessage.Clear)
                viewModel.deleteAllMessages()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        currentSavedStateHandle?.remove<Boolean>(EXTRA_ADD_MESSAGE)
        super.onDestroyView()
    }
}