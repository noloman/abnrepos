package com.nulltwenty.abnrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nulltwenty.abnrepos.domain.RepositoryListUseCase
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import com.nulltwenty.abnrepos.domain.model.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repositoryListUseCase: RepositoryListUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RepositoryListUiState(loading = true))
    val uiState: StateFlow<RepositoryListUiState> = _uiState.asStateFlow()

    init {
        getRepositoryList()
    }

    private fun getRepositoryList() {
        viewModelScope.launch {
            try {
                _uiState.update { state ->
                    when (val repositoryList = repositoryListUseCase.invoke()) {
                        is ResultOf.Error -> state.copy(
                            loading = false, error = repositoryList.exception.message
                        )
                        is ResultOf.Success -> state.copy(
                            loading = false, error = null, repositoryList = repositoryList.data
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { state -> state.copy(loading = false, error = e.message) }
            }
        }
    }
}

data class RepositoryListUiState(
    val loading: Boolean, val error: String? = null, val repositoryList: List<AbnRepo>? = null
)