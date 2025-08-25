package cypher.foodclub.core.utils.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    object RestaurantListScreen : Screens()
    @Serializable
    data class RestaurantDetailsScreen(val id: String) : Screens()
}