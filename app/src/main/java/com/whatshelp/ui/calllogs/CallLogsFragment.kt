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
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.Constants
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallLogsFragment : DBFragment<FragmentCallLogsBinding, CallLogsViewModel>() {

    override val viewModel: CallLogsViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_call_logs

    @Inject
    lateinit var callLogsAdapter: CallLogsAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.loadCallLogs()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied.
                toast("Goto settings and grand permissions")
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
        checkPermissionAndLoadCallLog()
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
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected.
                toast("Goto settings and grand permissions")
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CALL_LOG)
            }
        }
    }

}