package vn.finnest.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.finnest.app.data.market.MarketDataRepository
import vn.finnest.app.data.market.MarketQuoteDto

sealed interface MarketUiState {
    data object Idle : MarketUiState
    data object Loading : MarketUiState
    data class Success(val quotes: List<MarketQuoteDto>) : MarketUiState
    data class Error(val message: String) : MarketUiState
}

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val repository: MarketDataRepository
) : ViewModel() {
    private val _state = MutableStateFlow<MarketUiState>(MarketUiState.Idle)
    val state: StateFlow<MarketUiState> = _state.asStateFlow()

    val isConfigured: Boolean get() = repository.isConfigured()

    fun refresh(symbolsText: String) {
        val symbols = symbolsText.split(',', ' ', '\n', '\t').filter(String::isNotBlank)
        viewModelScope.launch {
            _state.value = MarketUiState.Loading
            _state.value = try {
                MarketUiState.Success(repository.quotes(symbols))
            } catch (error: Exception) {
                MarketUiState.Error(error.message ?: "Không thể tải dữ liệu thị trường.")
            }
        }
    }
}
