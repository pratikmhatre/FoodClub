package cypher.foodclub.core.domain

import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import retrofit2.Response

interface RestaurantsRepository {
    suspend fun getAllRestaurants(): Response<RestaurantsListResponse>
}