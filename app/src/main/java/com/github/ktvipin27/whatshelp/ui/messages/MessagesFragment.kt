package com.github.ktvipin27.whatshelp.ui.messages

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.databinding.FragmentMessagesBinding
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_HISTORY
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_MESSAGE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

        messagesAdapter.setItemDeleteListener { message ->
            messagesViewModel.onClickDeleteMessage(message)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}