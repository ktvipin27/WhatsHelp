package com.whatshelp.ui.messages

import android.os.Bundle
import android.view.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.whatshelp.R
import com.whatshelp.data.db.entity.Message
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.BaseFragment
import com.whatshelp.util.Constants.EXTRA_ADD_MESSAGE
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class MessagesFragment : BaseFragment<MessagesViewModel>() {

    override val viewModel: MessagesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MessagesScreen(onClickMessageItem = { message ->
                onClickMessage(message)
            }, onClickFab = {
                analyticsManager.logEvent(AnalyticsEvent.QuickMessage.New)
                findNavController().navigate(R.id.action_messagesFragment_to_addMessagesFragment)
            })
        }
    }

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

    private var currentSavedStateHandle: SavedStateHandle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        currentSavedStateHandle = navController.currentBackStackEntry?.savedStateHandle


        viewModel.messages.observe(viewLifecycleOwner, { messages ->
            setHasOptionsMenu(messages.isNotEmpty())
        })

        /*currentSavedStateHandle
            ?.getLiveData<Boolean>(EXTRA_ADD_MESSAGE)
            ?.observe( viewLifecycleOwner ) { isNewMessageAdded ->
                if (isNewMessageAdded)
                    binding.rvMessages.smoothScrollToPosition(messagesAdapter.itemCount)
            }*/
    }

    private fun onClickMessage(
        message: Message
    ) {
        analyticsManager.logEvent(AnalyticsEvent.QuickMessage.Click)
        with(findNavController()) {
            previousBackStackEntry?.savedStateHandle?.set(EXTRA_MESSAGE, message.text)
            navigateUp()
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