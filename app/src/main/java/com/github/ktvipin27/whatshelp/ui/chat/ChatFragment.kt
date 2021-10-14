package com.github.ktvipin27.whatshelp.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.ktvipin27.whatshelp.databinding.FragmentChatBinding
import com.github.ktvipin27.whatshelp.util.WhatsAppHelper


class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var whatsAppHelper: WhatsAppHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.btnSend.setOnClickListener {
            onClickWhatsApp(
                binding.etNumber.text.toString(),
                binding.etMessage.text.toString()
            )
        }

        whatsAppHelper = WhatsAppHelper(requireContext().applicationContext)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickWhatsApp(mobile: String, message: String) {
        val isWhatsappInstalled = whatsAppHelper.isWhatsAppInstalled()

        if (isWhatsappInstalled) {
            whatsAppHelper.sendMessage(mobile, message)
        } else {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                .show()
        }
    }
}