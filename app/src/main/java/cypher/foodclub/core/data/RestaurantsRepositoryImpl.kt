package cypher.foodclub.core.data

import cypher.foodclub.core.data.network.ApiList
import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import cypher.foodclub.core.data.network.models.RestaurantsListResponse.Restaurant
import cypher.foodclub.core.domain.RestaurantsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RestaurantsRepositoryImpl @Inject constructor(private val apiList: ApiList) : RestaurantsRepository {
    private var cachedRestaurants: List<Restaurant>? = null

    override suspend fun fetchAllRestaurantsFromServer(): Result<Unit> {
        try {
            if (cachedRestaurants.isNullOrEmpty().not()) return Result.success(Unit);

            val response = apiList.getAllRestaurantsList() // Assuming this is your API call
            if (response.isSuccessful && response.body() != null) {
                cachedRestaurants = response.body()!!.restaurants
                return Result.success(Unit)
            } else {
                return Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun getStoredRestaurantList(): List<Restaurant> {
        return cachedRestaurants ?: emptyList()
    }

    override fun getRestaurantDetailsById(id: String): Restaurant? {
        return getStoredRestaurantList().firstOrNull { it.id == id }
    }
}