package com.whatshelp.ui.calllogs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.whatshelp.R
import com.whatshelp.databinding.FragmentCallLogsBinding
import com.whatshelp.manager.app.AppManager
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.ui.dialogs.PermissionDialog
import com.whatshelp.util.Constants
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
            if (isGranted) {
                viewModel.loadCallLogs()
            } else {
                viewModel.setPermission(false)
            }
        }
    private val permissionListener = object : PermissionDialog.PermissionDialogListener {
        override fun onCancelled() {
            viewModel.setPermission(false)
        }

        override fun onProceed(isRationale: Boolean) {
            if (!isRationale)
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CALL_LOG)
            else
                appManager.openSettings()
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

        binding.btnPermissionOk.setOnClickListener { checkPermissionAndLoadCallLog() }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setPermission(false)
        } else {
            viewModel.loadCallLogs()
        }
    }

    private fun checkPermissionAndLoadCallLog() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.loadCallLogs()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) -> {
                viewModel.setPermission(false)
                PermissionDialog(permissionListener, true)
                    .show(childFragmentManager, "")
            }
            else -> {
                viewModel.setPermission(false)
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CALL_LOG)
            }
        }
    }

}