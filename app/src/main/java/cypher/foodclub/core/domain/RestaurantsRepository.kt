package cypher.foodclub.core.domain

import cypher.foodclub.core.data.network.models.RestaurantsListResponse.Restaurant


interface RestaurantsRepository {
    suspend fun fetchAllRestaurantsFromServer() : Result<Unit>
    fun getStoredRestaurantList(): List<Restaurant>
    fun getRestaurantDetailsById(id: String): Restaurant?
}