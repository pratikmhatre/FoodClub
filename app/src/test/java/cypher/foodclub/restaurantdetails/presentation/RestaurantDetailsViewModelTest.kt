package cypher.foodclub.restaurantdetails.presentation

import cypher.foodclub.core.data.network.models.RestaurantsListResponse
import cypher.foodclub.core.utils.toRestaurantDetailsModel
import cypher.foodclub.restaurantdetails.domain.models.Deal
import cypher.foodclub.restaurantdetails.domain.models.RestaurantDetailsModel
import cypher.foodclub.restaurantdetails.domain.usecases.GetRestaurantDetails
import cypher.foodclub.restaurantdetails.presentation.states.RestaurantDetailsUiStates
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RestaurantDetailsViewModelTest {

    private lateinit var viewModel: RestaurantDetailsViewModel
    private val getRestaurantDetails: GetRestaurantDetails = mockk()
    private val restaurantId = "1212"

    private fun simulateRestaurantDetailsNotFound() {
        every { getRestaurantDetails.invoke(restaurantId) } returns null
        viewModel = RestaurantDetailsViewModel(getRestaurantDetails)
    }

    private fun simulateRestaurantDetailsFound() {
        every { getRestaurantDetails.invoke(restaurantId) } returns getDummyRestaurantDetails()
        viewModel = RestaurantDetailsViewModel(getRestaurantDetails)
    }

    @Before
    fun setUp() {
        simulateRestaurantDetailsFound()
    }


    @Test
    fun `getUiState initial state`() = runTest {
        assert(viewModel.uiState.value == RestaurantDetailsUiStates.Loading)
    }

    @Test
    fun `setRestaurantId with valid ID leading to success`() {
        viewModel.setRestaurantId(restaurantId)
        assert(viewModel.uiState.value == RestaurantDetailsUiStates.Success(getDummyRestaurantDetails()))
    }

    @Test
    fun `setRestaurantId with valid ID gives restaurant details with deals sorted in descending order of discount`() {
        viewModel.setRestaurantId(restaurantId)
        assert(viewModel.uiState.value == RestaurantDetailsUiStates.Success(getDummyRestaurantDetails()))
        val restaurantDetails = (viewModel.uiState.value as RestaurantDetailsUiStates.Success).data
        assert(isSortedDescending(restaurantDetails.deals))
    }

    @Test
    fun `setRestaurantId with invalid ID leading to error`() {
        simulateRestaurantDetailsNotFound()
        viewModel.setRestaurantId(restaurantId)
        assert(viewModel.uiState.value == RestaurantDetailsUiStates.Error)
    }


    private fun isSortedDescending(list: List<Deal>): Boolean {
        if (list.size <= 1) {
            return true
        }
        for (i in 0 until list.size - 1) {
            if (list[i].discount.toDouble() < list[i + 1].discount.toDouble()) {
                return false
            }
        }
        return true
    }


    private fun getDummyRestaurantDetails(): RestaurantDetailsModel {
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
        return restaurant.toRestaurantDetailsModel()
    }

}