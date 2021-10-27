package com.whatshelp.ui.messages

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.R
import com.whatshelp.databinding.FragmentMessagesBinding
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import com.whatshelp.util.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private val messagesViewModel: MessagesViewModel by viewModels()

    private lateinit var binding: FragmentMessagesBinding

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

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

        messagesAdapter.setItemClickListener { message ->
            findNavController().let { navController ->
                navController.previousBackStackEntry?.savedStateHandle?.set(EXTRA_MESSAGE, message.text)
                navController.navigateUp()
            }
        }

        binding.rvMessages.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = messagesAdapter
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_messages_to_navigation_add_messages)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesViewModel.messages.observe(viewLifecycleOwner, { messages ->
            messagesAdapter.submitList(messages)
        })

        ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                messagesViewModel.deleteMessage(viewHolder.bindingAdapterPosition)
            }
        }).attachToRecyclerView(binding.rvMessages)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}