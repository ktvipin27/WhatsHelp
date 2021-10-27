package com.github.ktvipin27.whatshelp.ui.chat

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ktvipin27.whatshelp.MainViewModel
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.data.model.App
import com.github.ktvipin27.whatshelp.data.model.WhatsApp
import com.github.ktvipin27.whatshelp.data.model.WhatsAppBusiness
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import com.github.ktvipin27.whatshelp.databinding.FragmentChatBinding
import com.github.ktvipin27.whatshelp.util.*
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_HISTORY
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_MESSAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var whatsAppHelper: WhatsAppHelper

    @Inject
    lateinit var numberUtil: NumberUtil

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

        binding.tilMessage.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_navigation_messages)
        }

        binding.ccp.registerCarrierNumberEditText(binding.etNumber)

        binding.etNumber.setOnPasteListener { text -> setTextFromClipboard(text) }

        binding.btnSend.setOnClickListener { onClickAction(ChatAction.OPEN_CHAT) }
        binding.btnShareLink.setOnClickListener { onClickAction(ChatAction.SHARE_LINK) }

        binding.btg.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (checkedId == R.id.btn_whatsapp)
                chatViewModel.setSelectedApp(WhatsApp)
            else
                chatViewModel.setSelectedApp(WhatsAppBusiness)
        }

        initObservers()
    }

    private fun onClickAction(action: ChatAction) {
        with(binding.ccp) {
            chatViewModel.onClickAction(
                action, isValidFullNumber, selectedCountryCode, fullNumber, formattedFullNumber
            )
        }
    }

    private fun initObservers() {
        chatViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatState.OpenChat -> {
                    binding.root.hideKeyboard()
                    openChatApp(state.number, state.message, state.app)
                }
                is ChatState.ShareLink -> {
                    binding.root.hideKeyboard()
                    whatsAppHelper.shareLink(state.number, state.message)
                }
                ChatState.InvalidNumber -> toast(R.string.error_invalid_number)
                ChatState.InvalidData -> toast(R.string.error_no_data)
            }
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.let { handle ->
            handle.getLiveData<WhatsAppNumber>(EXTRA_HISTORY).observe(
                viewLifecycleOwner
            ) { wan ->
                handle.remove<WhatsAppNumber>(EXTRA_HISTORY)
                binding.ccp.setCountryForPhoneCode(wan.code ?: 0)
                binding.etNumber.setText(wan.number)
            }
            handle.getLiveData<String>(EXTRA_MESSAGE).observe(
                viewLifecycleOwner
            ) { message ->
                handle.remove<String>(EXTRA_MESSAGE)
                binding.etMessage.setText(message)
            }
        }

        mainViewModel.copiedNumberLiveData.observe(viewLifecycleOwner, { wan ->
            wan?.let { showCopiedNumberInfoDialog(wan) }
        })
    }

    private fun showCopiedNumberInfoDialog(
        wan: WhatsAppNumber,
    ) {
        AlertDialog
            .Builder(requireContext())
            .setMessage(getString(R.string.message_copied_number, wan.fullNumber))
            .setPositiveButton(R.string.action_use) { p0, _ ->
                p0.dismiss()
                wan.code?.let { code -> binding.ccp.setCountryForPhoneCode(code) }
                binding.etNumber.setText(wan.number)
            }
            .setNegativeButton(R.string.action_ignore) { p0, _ ->
                p0.dismiss()
            }
            .setOnDismissListener {
                mainViewModel.resetCopiedNumber()
            }
            .show()
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

    private fun openChatApp(mobile: String, message: String, app: App) {
        if (whatsAppHelper.appInstalledOrNot(app.packageName)) whatsAppHelper.openChat(mobile, message,app.packageName)
        else toast(getString(R.string.error_no_whatsapp,app.name))
    }
}