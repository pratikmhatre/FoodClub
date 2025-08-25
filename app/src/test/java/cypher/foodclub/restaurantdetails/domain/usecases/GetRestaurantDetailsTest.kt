package cypher.foodclub.restaurantdetails.domain.usecases

import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import cypher.foodclub.core.domain.RestaurantsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetRestaurantDetailsTest {
    private val repository: RestaurantsRepository = mockk()
    private lateinit var getRestaurantDetails: GetRestaurantDetails
    private val restaurantId = "123"
    private fun simulateSuccess() {
        every { repository.getRestaurantDetailsById(any()) } returns getDummyRestaurantDetails()
        getRestaurantDetails = GetRestaurantDetails(repository)
    }

    private fun simulateError() {
        every { repository.getRestaurantDetailsById(any()) } returns null
        getRestaurantDetails = GetRestaurantDetails(repository)
    }


    @Before
    fun setUp() {
        simulateSuccess()
    }

    @Test
    fun `invoke with valid restaurantId`() {
        val result = getRestaurantDetails.invoke(restaurantId)
        assert(result != null)
    }

    @Test
    fun `invoke with non existent restaurantId`() {
        simulateError()
        val result = getRestaurantDetails.invoke(restaurantId)
        assert(result == null)
    }

    @Test
    fun `invoke with empty restaurantId`() {
        simulateError()
        val result = getRestaurantDetails.invoke("")
        assert(result == null)
    }

    @Test
    fun `invoke calls getRestaurantDetailsById function of repository`() {
        getRestaurantDetails.invoke(restaurantId)
        verify(exactly = 1) { repository.getRestaurantDetailsById(restaurantId) }
    }


    private fun getDummyRestaurantDetails(): RestaurantsListResponse.Restaurant {
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
        val restaurant = RestaurantsListResponse.Restaurant(
            id = "res_001",
            name = "Noodle House",
            address = "12 Wok Street",
            closesAt = "22:00",
            cuisines = listOf("Chinese", "Thai", "Asian Fusion"),
            deals = dummyDealsSet1,
            imageLink = "https://example.com/images/noodle_house.jpg",
            opensAt = "11:00",
            suburb = "Downtown"
        )
        return restaurant
    }

}