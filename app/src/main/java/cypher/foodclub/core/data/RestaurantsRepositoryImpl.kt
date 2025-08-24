package cypher.foodclub.core.data

import cypher.foodclub.core.data.network.ApiList
import cypher.foodclub.core.domain.RestaurantsRepository
import javax.inject.Inject

class RestaurantsRepositoryImpl @Inject constructor(private val apiList: ApiList) : RestaurantsRepository {
    override suspend fun getAllRestaurants() = apiList.getAllRestaurantsList()
}