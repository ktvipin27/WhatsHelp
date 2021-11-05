package com.whatshelp.ui.calllogs

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.whatshelp.R
import com.whatshelp.databinding.FragmentCallLogsBinding
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallLogsFragment : DBFragment<FragmentCallLogsBinding, CallLogsViewModel>() {

    override val viewModel: CallLogsViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_call_logs

    @Inject
    lateinit var callLogsAdapter: CallLogsAdapter

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
    }

}