package cypher.foodclub.restaurantlist.presentation.states

sealed class RestaurantsUiEvents {
    data class OpenRestaurantDetails(val id: String) : RestaurantsUiEvents()
}