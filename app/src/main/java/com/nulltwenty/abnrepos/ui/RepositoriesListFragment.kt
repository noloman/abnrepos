package com.nulltwenty.abnrepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nulltwenty.abnrepos.R
import kotlinx.coroutines.launch

class RepositoriesListFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    private lateinit var viewModel: RepositoriesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.repositoryListRecyclerView)
        val loadingProgressIndicator =
            view.findViewById<CircularProgressIndicator>(R.id.loadingProgressIndicator)
        val adapter = RepositoryListAdapter()
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeAndCollectChanges(loadingProgressIndicator, adapter)
            }
        }
        return view
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RepositoriesListViewModel::class.java]
        // TODO: Use the ViewModel
    }
}