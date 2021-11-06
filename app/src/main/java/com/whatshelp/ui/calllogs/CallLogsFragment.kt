package com.whatshelp.ui.calllogs

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.whatshelp.R
import com.whatshelp.databinding.FragmentCallLogsBinding
import com.whatshelp.manager.app.AppManager
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.ui.dialogs.PermissionDialog
import com.whatshelp.util.Constants
import com.whatshelp.util.hasPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallLogsFragment : DBFragment<FragmentCallLogsBinding, CallLogsViewModel>() {

    override val viewModel: CallLogsViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_call_logs

    @Inject
    lateinit var callLogsAdapter: CallLogsAdapter

    @Inject
    lateinit var appManager: AppManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            onPermissionResult(isGranted)
        }

    private val permissionListener = object : PermissionDialog.PermissionDialogListener {
        override fun onCancelled() {
            viewModel.setPermission(false)
        }

        override fun onProceed(isSettings: Boolean) {
            if (isSettings)
                appManager.openSettings()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
        }
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

        binding.btnPermissionOk.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
        }

    }

    private fun onPermissionResult(isGranted: Boolean) {
        when {
            isGranted -> {
                viewModel.loadCallLogs()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) -> {
                viewModel.setPermission(false)
                PermissionDialog(permissionListener, false)
                    .show(childFragmentManager, "")
            }
            else -> {
                viewModel.setPermission(false)
                PermissionDialog(permissionListener, true)
                    .show(childFragmentManager, "")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireContext().hasPermission(Manifest.permission.READ_CALL_LOG)) {
            viewModel.loadCallLogs()
        } else {
            viewModel.setPermission(false)
        }
    }

    override fun onDestroyView() {
        requestPermissionLauncher.unregister()
        super.onDestroyView()
    }

}