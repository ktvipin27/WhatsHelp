package com.whatshelp.ui.calllogs

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.markodevcic.peko.PermissionResult
import com.markodevcic.peko.requestPermissionsAsync
import com.whatshelp.R
import com.whatshelp.databinding.FragmentCallLogsBinding
import com.whatshelp.manager.app.AppManager
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.ui.dialogs.PermissionDialog
import com.whatshelp.util.Constants
import com.whatshelp.util.hasPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallLogsFragment : DBFragment<FragmentCallLogsBinding, CallLogsViewModel>() {

    override val viewModel: CallLogsViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_call_logs

    @Inject
    lateinit var callLogsAdapter: CallLogsAdapter

    @Inject
    lateinit var appManager: AppManager

    private var isSettingsOpened = false

    private val permissionDialog by lazy {
        PermissionDialog({ checkPermission() }, { viewModel.setPermissionGranted(false) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callLogsAdapter.setItemClickListener { callLog ->
            findNavController().let { navController ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Constants.EXTRA_NUMBER, callLog.number)
                navController.navigateUp()
            }
        }

        binding.rvCallLogs.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = callLogsAdapter
        }

        viewModel.callLogs.observe(viewLifecycleOwner, { callLog ->
            callLogsAdapter.submitList(callLog)
        })

        binding.btnPermissionAction.setOnClickListener {
            appManager.openSettings().also { isSettingsOpened = true }
        }

        checkPermission()
    }

    private fun checkPermission() {
        lifecycleScope.launch {
            when (requestPermissionsAsync(Manifest.permission.READ_CALL_LOG)) {
                is PermissionResult.Granted -> viewModel.setPermissionGranted(true)
                is PermissionResult.Denied.NeedsRationale -> {
                    permissionDialog
                        .show(childFragmentManager, "")
                }
                else -> viewModel.setPermissionGranted(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSettingsOpened) {
            isSettingsOpened = false
            val hasPermission = requireContext().hasPermission(Manifest.permission.READ_CALL_LOG)
            viewModel.setPermissionGranted(hasPermission)
        }
    }

}