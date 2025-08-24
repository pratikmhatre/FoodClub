package cypher.foodclub.core.data.network

import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import cypher.foodclub.core.utils.AppConstants
import retrofit2.Response
import retrofit2.http.GET

interface ApiList {
    @GET(AppConstants.RESTAURANTS_ENDPOINT)
    suspend fun getAllRestaurantsList(): Response<RestaurantsListResponse>
}