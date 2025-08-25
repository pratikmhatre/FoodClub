package cypher.foodclub.restaurantdetails.domain.usecases

import cypher.foodclub.core.domain.RestaurantsRepository
import cypher.foodclub.core.utils.toRestaurantDetailsModel
import cypher.foodclub.restaurantdetails.domain.models.RestaurantDetailsModel
import javax.inject.Inject

class GetRestaurantDetails @Inject constructor(private val repository: RestaurantsRepository) {
    operator fun invoke(restaurantId: String): RestaurantDetailsModel? {
        return repository.getRestaurantDetailsById(restaurantId)?.toRestaurantDetailsModel()
    }
}