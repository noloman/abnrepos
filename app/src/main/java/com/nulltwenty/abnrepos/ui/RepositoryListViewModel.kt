package com.nulltwenty.abnrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nulltwenty.abnrepos.data.GithubPagingSource
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val githubPagingSource: GithubPagingSource) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RepositoryListUiState(loading = true))
    val uiState: StateFlow<RepositoryListUiState> = _uiState.asStateFlow()
    val items: Flow<PagingData<AbnRepo>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { githubPagingSource }).flow.cachedIn(viewModelScope)
}

data class RepositoryListUiState(
    val loading: Boolean, val error: String? = null, val repositoryList: List<AbnRepo>? = null
)