package com.github.ktvipin27.whatshelp.ui.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.databinding.FragmentChatBinding
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_HISTORY
import com.github.ktvipin27.whatshelp.util.NumberUtil
import com.github.ktvipin27.whatshelp.util.WhatsAppHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

        binding.tilNumber.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_navigation_history)
        }

        binding.ccp.registerCarrierNumberEditText(binding.etNumber)

        binding.etNumber.setOnPasteListener { text ->
            lifecycleScope.launch {
                numberUtil.isValidFullNumber(text).collect {
                    when(it){
                        NumberUtil.NumberValidity.Invalid -> binding.etNumber.setText(text)
                        is NumberUtil.NumberValidity.Valid -> {
                            binding.ccp.setCountryForPhoneCode(it.code)
                            binding.etNumber.setText(it.number)
                        }
                    }
                }
            }
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

        findNavController().currentBackStackEntry?.savedStateHandle?.let { handle ->
            handle.getLiveData<History>(EXTRA_HISTORY).observe(
                viewLifecycleOwner
            ) { history ->
                handle.remove<History>(EXTRA_HISTORY)
                chatViewModel.onReceiveHistory(history)
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

    private fun hideKeyboard() {
        binding.btnSend.let { view ->
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}