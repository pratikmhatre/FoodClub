package cypher.foodclub.restaurantdetails.presentation

import androidx.lifecycle.ViewModel
import cypher.foodclub.restaurantdetails.domain.usecases.GetRestaurantDetails
import cypher.foodclub.restaurantdetails.presentation.states.RestaurantDetailsUiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(private val getRestaurantDetails: GetRestaurantDetails) :
    ViewModel() {
    private var restaurantId: String? = null
    private val _uiState = MutableStateFlow<RestaurantDetailsUiStates>(RestaurantDetailsUiStates.Loading)
    val uiState: StateFlow<RestaurantDetailsUiStates> = _uiState

    fun setRestaurantId(id: String) {
        restaurantId = id
        getRestaurantDetailsById(id)
    }

    private fun getRestaurantDetailsById(restaurantId: String) {
        val details = getRestaurantDetails(restaurantId)
        if (details == null) {
            _uiState.value = RestaurantDetailsUiStates.Error
        } else {
            _uiState.value = RestaurantDetailsUiStates.Success(details)
        }
    }
}