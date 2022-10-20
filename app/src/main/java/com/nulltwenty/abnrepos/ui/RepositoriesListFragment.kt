package com.nulltwenty.abnrepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nulltwenty.abnrepos.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    private val viewModel: RepositoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.repositoryListRecyclerView)
        val loadingProgressIndicator =
            view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator)
        val adapter = RepositoryListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeAndCollectChanges(loadingProgressIndicator, adapter)
            }
        }
    }

    private suspend fun observeAndCollectChanges(
        loadingProgressIndicator: CircularProgressIndicator, adapter: RepositoryListAdapter
    ) {
        viewModel.uiState.collect {
            when {
                it.loading -> loadingProgressIndicator.visibility = View.VISIBLE
                it.error != null -> loadingProgressIndicator.visibility = View.GONE
                it.repositoryList?.isNotEmpty() == true -> {
                    loadingProgressIndicator.visibility = View.GONE
                    adapter.submitList(it.repositoryList)
                }
            }
        }
    }
}