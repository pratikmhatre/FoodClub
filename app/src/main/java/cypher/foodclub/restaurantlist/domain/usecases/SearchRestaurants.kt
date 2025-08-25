package cypher.foodclub.restaurantlist.domain.usecases

import cypher.foodclub.core.domain.RestaurantsRepository
import cypher.foodclub.core.utils.toRestaurantListDisplayModel
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel
import javax.inject.Inject

class SearchRestaurants @Inject constructor(private val repository: RestaurantsRepository) {
    operator fun invoke(query: String): List<RestaurantListDisplayModel> {
        val restaurantsList = repository.getStoredRestaurantList().toRestaurantListDisplayModel()
        val searchResults = restaurantsList.filter {
            it.name.lowercase().contains(query.lowercase()) || it.cuisines.any {
                it.lowercase().contains(query.lowercase())
            }
        }
        return searchResults
    }
}