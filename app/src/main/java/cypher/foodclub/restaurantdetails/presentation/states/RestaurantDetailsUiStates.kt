package cypher.foodclub.restaurantdetails.presentation.states

import cypher.foodclub.restaurantdetails.domain.models.RestaurantDetailsModel

sealed class RestaurantDetailsUiStates {
    object Loading : RestaurantDetailsUiStates()
    data class Success(val data: RestaurantDetailsModel) : RestaurantDetailsUiStates()
    object Error : RestaurantDetailsUiStates()
}