package com.whatshelp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whatshelp.R
import com.whatshelp.databinding.DialogPermissionBinding
import com.whatshelp.util.toast

/**
 * Created by Vipin KT on 05/11/21
 */
class PermissionDialog(
    private val successListener: () -> Unit,
    private val dismissListener: () -> Unit,
) : DialogFragment() {

    private var _binding: DialogPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPermissionBinding.inflate(LayoutInflater.from(context))
        binding.btnNegative.setOnClickListener {
            dismissListener()
            dismiss()
        }
        binding.btnPositive.setOnClickListener {
            successListener()
            dismiss()
        }
        binding.ivIcon.setImageResource(R.drawable.ic_baseline_call_24)
        val s = StringBuilder(getString(R.string.message_permission_call_history))
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
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {
            toast(getString(R.string.message_something_went_wrong))
        }

    }
}