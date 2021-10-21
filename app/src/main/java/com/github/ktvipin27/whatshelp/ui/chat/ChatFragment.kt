package com.github.ktvipin27.whatshelp.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.databinding.FragmentChatBinding
import com.github.ktvipin27.whatshelp.util.ClipboardUtil
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_HISTORY
import com.github.ktvipin27.whatshelp.util.NumberUtil
import com.github.ktvipin27.whatshelp.util.WhatsAppHelper
import com.github.ktvipin27.whatshelp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var whatsAppHelper: WhatsAppHelper

    @Inject
    lateinit var numberUtil: NumberUtil

    @Inject
    lateinit var clipboardUtil: ClipboardUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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

        binding.tilNumber.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_navigation_history)
        }

        binding.ccp.registerCarrierNumberEditText(binding.etNumber)

        binding.etNumber.setOnPasteListener { text -> setTextFromClipboard(text) }

        binding.btnSend.setOnClickListener {
            with(binding.ccp) {
                chatViewModel.onClickSend(selectedCountryCode, fullNumber, formattedFullNumber)
            }
        }

        initObservers()
    }

    private fun initObservers() {
        chatViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatState.OpenWhatsApp -> {
                    binding.root.hideKeyboard()
                    openWhatsApp(state.number, state.message)
                }
            }
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.let { handle ->
            handle.getLiveData<History>(EXTRA_HISTORY).observe(
                viewLifecycleOwner
            ) { history ->
                handle.remove<History>(EXTRA_HISTORY)
                binding.ccp.setCountryForPhoneCode(history.code.toInt())
                binding.etNumber.setText(history.number)
            }
        }
    }

    private fun setTextFromClipboard(text: String) {
        lifecycleScope.launch {
            when (val numberValidity = numberUtil.isValidFullNumber(text)) {
                is NumberUtil.NumberValidity.ValidNumber -> {
                    binding.ccp.setCountryForPhoneCode(numberValidity.code)
                    binding.etNumber.setText(numberValidity.number)
                }
                is NumberUtil.NumberValidity.InvalidCountryCode -> binding.etNumber.setText(
                    numberValidity.number)
                NumberUtil.NumberValidity.InvalidNumber -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.ccp.deregisterCarrierNumberEditText()
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
}