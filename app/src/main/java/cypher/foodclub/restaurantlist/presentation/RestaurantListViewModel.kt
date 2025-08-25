package cypher.foodclub.restaurantlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cypher.foodclub.core.utils.customdispatcher.DispatcherProvider
import cypher.foodclub.restaurantlist.domain.usecases.GetRestaurantsListData
import cypher.foodclub.restaurantlist.domain.usecases.SearchRestaurants
import cypher.foodclub.restaurantlist.presentation.states.RestaurantsUiEvents
import cypher.foodclub.restaurantlist.presentation.states.RestaurantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getRestaurantsList: GetRestaurantsListData, private val searchRestaurants: SearchRestaurants
) : ViewModel() {
    private val _uiState = MutableStateFlow(RestaurantsUiState())
    val uiState: StateFlow<RestaurantsUiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<RestaurantsUiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        fetchRestaurantsList()
    }

    private fun fetchRestaurantsList() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch(dispatcherProvider.io) {
            val result = getRestaurantsList.invoke()
            if (result.isSuccess) {
                if (result.getOrNull()!!.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(isLoading = false, displayedRestaurants = result.getOrNull()!!)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "No restaurants found!")
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Something went wrong!"
                )
            }
        }
    }

    fun onRestaurantClicked(id: String) {
        viewModelScope.launch(dispatcherProvider.io) { _uiEvents.emit(RestaurantsUiEvents.OpenRestaurantDetails(id)) }
    }

    fun searchStoredRestaurants(query: String) {
        _uiState.value = _uiState.value.copy(currentSearchQuery = query)
        if (query.isEmpty()) {
            fetchRestaurantsList()
            return
        }
        val searchResults = searchRestaurants(query)
        _uiState.value = _uiState.value.copy(displayedRestaurants = searchResults)
    }
}