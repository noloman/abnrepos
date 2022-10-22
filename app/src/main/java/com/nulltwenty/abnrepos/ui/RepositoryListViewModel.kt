package com.nulltwenty.abnrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nulltwenty.abnrepos.data.db.Repo
import com.nulltwenty.abnrepos.domain.GetAbnAmroReposUseCase
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val getAbnAmroReposUseCase: GetAbnAmroReposUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RepositoryListUiState())
    val uiState: StateFlow<RepositoryListUiState> = _uiState.asStateFlow()

    init {
        getRepositoryList()
    }

    private fun getRepositoryList() = viewModelScope.launch {
        try {
            getAbnAmroReposUseCase.invoke().cachedIn(this)
                .collect { pagingData: PagingData<Repo> ->
                    _uiState.update {
                        it.copy(error = null, repositoryList = pagingData.map { repo ->
                            return@map repo.toDomainModel()
                        })
                    }
                }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(error = e.message)
            }
        }
    }
}

data class RepositoryListUiState(
    val error: String? = null, val repositoryList: PagingData<AbnRepo>? = null
)

private fun Repo.toDomainModel(): AbnRepo =
    AbnRepo(id, name, fullName, avatarUrl, description, htmlUrl, visibility)
