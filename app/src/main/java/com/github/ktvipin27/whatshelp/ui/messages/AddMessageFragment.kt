package com.github.ktvipin27.whatshelp.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.databinding.FragmentAddMessageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Vipin KT on 26/10/21
 */
@AndroidEntryPoint
class AddMessageFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentAddMessageBinding

    private val messagesViewModel: MessagesViewModel by viewModels()

    private val emptyMessageError by lazy { getString(R.string.error_no_message) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDismiss.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            onClickSave()
        }
        binding.etMessage.doAfterTextChanged {
            if (binding.tilMessage.error.isNullOrEmpty().not() && it.toString().isNotBlank()) {
                binding.tilMessage.error = ""
            }
        }
    }

    private fun onClickSave() {
        val text = binding.etMessage.text.toString()
        if (text.isEmpty() || text.isBlank())
            binding.tilMessage.error = emptyMessageError
        else {
            binding.tilMessage.error = ""
            messagesViewModel.addMessage(text)
            dismiss()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}