package com.github.ktvipin27.whatshelp.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.databinding.FragmentChatBinding
import com.github.ktvipin27.whatshelp.util.WhatsAppHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var whatsAppHelper: WhatsAppHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.apply {
            viewModel = chatViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatState.OpenWhatsApp -> {
                    hideKeyboard()
                    openWhatsApp(state.number, state.message)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    private fun openWhatsApp(mobile: String, message: String) {
        val isWhatsappInstalled = whatsAppHelper.isWhatsAppInstalled()

        if (isWhatsappInstalled) {
            whatsAppHelper.sendMessage(mobile, message)
        } else {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun hideKeyboard(){
        binding.btnSend.let { view ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}