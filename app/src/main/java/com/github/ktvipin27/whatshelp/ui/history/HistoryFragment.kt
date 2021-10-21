package com.github.ktvipin27.whatshelp.ui.history

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.databinding.FragmentHistoryBinding
import com.github.ktvipin27.whatshelp.util.Constants.EXTRA_HISTORY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var binding: FragmentHistoryBinding

    @Inject
    lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        binding.apply {
            viewModel = historyViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        historyAdapter.setItemClickListener { history ->
            findNavController().let { navController ->
                navController.previousBackStackEntry?.savedStateHandle?.set(EXTRA_HISTORY, history)
                navController.navigateUp()
            }
        }

        binding.rvHistory.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = historyAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.history.observe(viewLifecycleOwner, { history ->
            historyAdapter.submitList(history)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}