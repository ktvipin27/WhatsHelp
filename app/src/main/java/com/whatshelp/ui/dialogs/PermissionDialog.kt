package com.whatshelp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whatshelp.R
import com.whatshelp.databinding.DialogPermissionBinding

/**
 * Created by Vipin KT on 05/11/21
 */
class PermissionDialog(
    private val listener: PermissionDialogListener,
    private var isSettings: Boolean,
) : DialogFragment() {

    private var _binding: DialogPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPermissionBinding.inflate(LayoutInflater.from(context))
        binding.btnNegative.setOnClickListener {
            listener.onCancelled()
            dismiss()
        }
        binding.btnPositive.setOnClickListener {
            listener.onProceed(isSettings)
            dismiss()
        }
        binding.ivIcon.setImageResource(R.drawable.ic_baseline_call_24)
        val s = StringBuilder(getString(R.string.message_permission_call_history))
        if (isSettings) {
            s.append("\n\n${getString(R.string.message_permission_call_history_rationale)}")
            binding.btnPositive.text = getString(R.string.action_permission_dialog_settings)
        } else {
            binding.btnPositive.text = getString(R.string.action_permission_dialog_continue)
        }
        binding.tvContent.text = s
        isCancelable = false
        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        dialog?.window?.setLayout(100, 100)
    }

    interface PermissionDialogListener {
        fun onCancelled()
        fun onProceed(isSettings: Boolean)
    }
}