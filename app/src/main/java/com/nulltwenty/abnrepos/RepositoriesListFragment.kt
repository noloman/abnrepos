package com.nulltwenty.abnrepos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RepositoriesListFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    private lateinit var viewModel: RepositoriesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RepositoriesListViewModel::class.java]
        // TODO: Use the ViewModel
    }
}