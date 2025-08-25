package cypher.foodclub.restaurantlist.presentation.states

import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel

data class RestaurantsUiState(
    val displayedRestaurants: List<RestaurantListDisplayModel> = emptyList(),
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val isSearchError: Boolean = false,
    val currentSearchQuery: String = "",
)