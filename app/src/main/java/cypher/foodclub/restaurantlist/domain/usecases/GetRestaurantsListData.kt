package cypher.foodclub.restaurantlist.domain.usecases

import cypher.foodclub.core.domain.RestaurantsRepository
import cypher.foodclub.core.utils.toRestaurantListDisplayModel
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel
import javax.inject.Inject

class GetRestaurantsListData @Inject constructor(private val repository: RestaurantsRepository) {
    operator suspend fun invoke(): Result<List<RestaurantListDisplayModel>> {
        val apiResult = repository.fetchAllRestaurantsFromServer()
        if (apiResult.isFailure) {
            return Result.failure(apiResult.exceptionOrNull()!!)
        } else {
            val cachedRestaurants = repository.getStoredRestaurantList()
            val displayList = cachedRestaurants.toRestaurantListDisplayModel().sortedBy {
                it.bestDeal.bestDiscount.toDouble()
            }
            return Result.success(displayList)
        }
    }
}