package com.nulltwenty.abnrepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nulltwenty.abnrepos.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoryListFragment : Fragment() {
    private val viewModel: RepositoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.repositoryListRecyclerView)
        val loadingProgressIndicator =
            view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator)
        val adapter = RepositoryListAdapter {
            val action: NavDirections =
                RepositoryListFragmentDirections.actionRepositoriesListFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private suspend fun observeAndCollectChanges(
        loadingProgressIndicator: CircularProgressIndicator, adapter: RepositoryListAdapter
    ) {
        viewModel.uiState.collect {
            when {
                it.loading -> loadingProgressIndicator.visibility = View.VISIBLE
                it.error != null -> {
                    loadingProgressIndicator.visibility = View.GONE
                    AlertDialog.Builder(requireContext()).setMessage("Unexpected error")
                        .setNegativeButton(getString(android.R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                            requireActivity().finish()
                        }.show()
                }
                it.repositoryList?.isNotEmpty() == true -> {
                    loadingProgressIndicator.visibility = View.GONE
//                    adapter.submitList(it.repositoryList)
                }
            }
        }
    }
}