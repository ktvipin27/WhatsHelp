package com.whatshelp.ui.messages

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.R
import com.whatshelp.data.model.WhatsAppNumber
import com.whatshelp.databinding.FragmentMessagesBinding
import com.whatshelp.util.Constants
import com.whatshelp.util.Constants.EXTRA_ADD_MESSAGE
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import com.whatshelp.util.SwipeToDeleteCallback
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private val messagesViewModel: MessagesViewModel by viewModels()

    private lateinit var binding: FragmentMessagesBinding

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

    private var currentSavedStateHandle: SavedStateHandle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)

        binding.apply {
            viewModel = messagesViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        currentSavedStateHandle = navController.currentBackStackEntry?.savedStateHandle

        messagesAdapter.setItemClickListener { message ->
            with(navController) {
                previousBackStackEntry?.savedStateHandle?.set(EXTRA_MESSAGE, message.text)
                navigateUp()
            }
        }

        ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                messagesViewModel.deleteMessage(viewHolder.bindingAdapterPosition)
            }
        }).attachToRecyclerView(binding.rvMessages)

        binding.run {
            rvMessages.apply {
                addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
                adapter = messagesAdapter
            }

            fab.setOnClickListener {
                navController.navigate(R.id.action_navigation_messages_to_navigation_add_messages)
            }

            fab.postDelayed({fab.show()},500)
        }

        messagesViewModel.messages.observe(viewLifecycleOwner, { messages ->
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
        inflater.inflate(R.menu.menu_delete_all, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> messagesViewModel.deleteAllMessages()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        currentSavedStateHandle?.remove<Boolean>(EXTRA_ADD_MESSAGE)
    }
}