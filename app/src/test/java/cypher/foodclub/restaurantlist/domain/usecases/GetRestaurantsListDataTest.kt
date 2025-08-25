package cypher.foodclub.restaurantlist.domain.usecases

import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import cypher.foodclub.core.domain.RestaurantsRepository
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRestaurantsListDataTest {

    private val repository: RestaurantsRepository = mockk()
    private lateinit var getRestaurantsListData: GetRestaurantsListData

    @Before
    fun setUp() {
        simulateSuccess()
    }

    private fun simulateSuccess() {
        coEvery { repository.fetchAllRestaurantsFromServer() } returns Result.success(Unit)
        every { repository.getStoredRestaurantList() } returns generateDummyRestaurantsForTesting()
        getRestaurantsListData = GetRestaurantsListData(repository)
    }

    private fun simulateFailure() {
        coEvery { repository.fetchAllRestaurantsFromServer() } returns Result.failure(Exception())
        every { repository.getStoredRestaurantList() } returns emptyList()
        getRestaurantsListData = GetRestaurantsListData(repository)
    }

    fun generateDummyRestaurantsForTesting(): List<RestaurantsListResponse.Restaurant> {
        val dummyDealsSet1 = listOf(
            RestaurantsListResponse.Restaurant.Deal(
                id = "deal_01_a",
                dineIn = "true",
                discount = "15",
                lightning = "false",
                opensAt = "18:00",
                closesAt = "20:00",
                qtyLeft = "50",
                start = "2023-01-01T00:00:00Z",
                end = "2024-12-31T23:59:59Z"
            ),
            RestaurantsListResponse.Restaurant.Deal(
                id = "deal_01_b",
                dineIn = "false", // Delivery only
                discount = "5",
                lightning = "true",
                opensAt = null, // Always available
                closesAt = null,
                qtyLeft = "100",
                start = "2023-05-01T00:00:00Z",
                end = "2024-08-31T23:59:59Z"
            )
        )

        val dummyDealsSet2 = listOf(
            RestaurantsListResponse.Restaurant.Deal(
                id = "deal_02_a",
                dineIn = "true",
                discount = "40",
                lightning = "false",
                opensAt = "12:00",
                closesAt = "14:00", // Lunch special
                qtyLeft = "20",
                start = "2023-03-15T00:00:00Z",
                end = null // Ongoing
            )
        )

        return listOf(
            RestaurantsListResponse.Restaurant(
                id = "res_001",
                name = "Noodle House",
                address = "12 Wok Street",
                closesAt = "22:00",
                cuisines = listOf("Chinese", "Thai", "Asian Fusion"),
                deals = dummyDealsSet1,
                imageLink = "https://example.com/images/noodle_house.jpg",
                opensAt = "11:00",
                suburb = "Downtown"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_002",
                name = "Pizza Palace",
                address = "8 Slice Avenue",
                closesAt = "23:00",
                cuisines = listOf("Pizza", "Italian"),
                deals = dummyDealsSet2,
                imageLink = "https://example.com/images/pizza_palace.jpg",
                opensAt = "10:30",
                suburb = "Westside"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_003",
                name = "Curry Corner",
                address = "45 Spice Lane",
                closesAt = "21:30",
                cuisines = listOf("Indian", "Pakistani"),
                deals = dummyDealsSet2,
                imageLink = "https://example.com/images/curry_corner.jpg",
                opensAt = "12:00",
                suburb = "East Market"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_004",
                name = "The Burger Joint",
                address = "9 Patty Place",
                closesAt = "00:00", // Midnight
                cuisines = listOf("American", "Burgers", "Fast Food"),
                deals = dummyDealsSet1,
                imageLink = "https://example.com/images/burger_joint.jpg",
                opensAt = "09:00",
                suburb = "Southbank"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_005",
                name = "Sushi Express",
                address = "27 Rice Bowl Road",
                closesAt = "22:00",
                cuisines = listOf("Japanese", "Sushi"),
                deals = dummyDealsSet1,
                imageLink = "https://example.com/images/sushi_express.jpg",
                opensAt = "11:30",
                suburb = "Central Station"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_006",
                name = "Mediterranean Flavors",
                address = "1 Olive Grove",
                closesAt = "21:00",
                cuisines = listOf("Greek", "Mediterranean"),
                deals = dummyDealsSet1,
                imageLink = "https://example.com/images/mediterranean.jpg",
                opensAt = "10:00",
                suburb = "North End"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_007",
                name = "Vegan Vibes",
                address = "77 Greenleaf Way",
                closesAt = "20:00",
                cuisines = listOf("Vegan", "Healthy"),
                deals = dummyDealsSet2,
                imageLink = "https://example.com/images/vegan_vibes.jpg",
                opensAt = "09:30",
                suburb = "Green Quarter"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_008",
                name = "Steakhouse Supreme",
                address = "1 Prime Cut Alley",
                closesAt = "23:30",
                cuisines = listOf("Steakhouse", "American"),
                deals = dummyDealsSet1, // Reusing a deal set
                imageLink = "https://example.com/images/steakhouse.jpg",
                opensAt = "17:00", // Opens later
                suburb = "Financial District"
            ),
            RestaurantsListResponse.Restaurant(
                id = "res_009",
                name = "Tapas Time",
                address = "3 Small Plate Street",
                closesAt = "01:00", // Late night
                cuisines = listOf("Spanish", "Tapas"),
                deals = dummyDealsSet1,
                imageLink = "https://example.com/images/tapas_time.jpg",
                opensAt = "16:00",
                suburb = "Old Town"
            )
        )
    }


    @Test
    fun `getRestaurantDetails when success`() = runTest {
        simulateSuccess()
        val result = getRestaurantsListData()
        assert(result.isSuccess)
        assert(result.getOrNull()?.isNotEmpty() == true)
    }

    @Test
    fun `getRestaurantDetails when failure`() = runTest {
        simulateFailure()
        val result = getRestaurantsListData()
        assert(result.isFailure)
    }

    @Test
    fun `getRestaurantDetails returns sorted restaurants with highest discount first`() = runTest {
        simulateSuccess()
        val result = getRestaurantsListData()
        assert(result.isSuccess)
        assert(result.getOrNull() != null)
        assert(isSortedDescending(result.getOrNull()!!))
    }

    private fun isSortedDescending(list: List<RestaurantListDisplayModel>): Boolean {
        if (list.size <= 1) {
            return true
        }
        for (i in 0 until list.size - 1) {
            if (list[i].bestDeal.bestDiscount.toInt() < list[i + 1].bestDeal.bestDiscount.toInt()) {
                return false
            }
        }
        return true
    }
}