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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nulltwenty.abnrepos.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.beryukhov.reactivenetwork.ReactiveNetwork
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryListFragment : Fragment() {
    private val viewModel: RepositoryListViewModel by viewModels()

    @Inject
    lateinit var reactiveNetwork: ReactiveNetwork
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.repositoryListRecyclerView)
        val adapter = RepositoryListAdapter {
            val action: NavDirections =
                RepositoryListFragmentDirections.actionRepositoriesListFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }
        addPagingAdapterLoadStateListener(adapter, view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeNetworkConnectivity(view, adapter)
                collectionFromViewModelUiState(view, adapter)
            }
        }
    }

    private suspend fun observeNetworkConnectivity(view: View, adapter: RepositoryListAdapter) {
        reactiveNetwork.observeNetworkConnectivity(requireContext()).collect { connectivity ->
            if (connectivity.available) {
                fetchAndCollectData(view, adapter)
            }
        }
    }

    private suspend fun fetchAndCollectData(view: View, adapter: RepositoryListAdapter) {
        viewModel.getRepositoryList()
        collectionFromViewModelUiState(view, adapter)
    }

    private suspend fun collectionFromViewModelUiState(view: View, adapter: RepositoryListAdapter) {
        viewModel.uiState.collect {
            when {
                it.error != null -> hideProgressAndShowErrorDialog(view)
                it.repositoryList != null -> adapter.submitData(it.repositoryList)
            }
        }
    }

    /**
     * Adds a listener to the load states of the [adapter] that will help us model the UI state
     */
    private fun addPagingAdapterLoadStateListener(
        adapter: RepositoryListAdapter, view: View
    ) {
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator).visibility =
                        View.VISIBLE
                }
                !is LoadState.Loading -> {
                    view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator).visibility =
                        View.GONE
                }
            }
        }
    }

    /**
     * Hides the CircularProgressIndicator and shows an [AlertDialog] is there's an error
     */
    private fun hideProgressAndShowErrorDialog(view: View) {
        view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator).visibility =
            View.GONE
        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.alert_dialog_error_title))
            .setNegativeButton(getString(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }.show()
    }
}