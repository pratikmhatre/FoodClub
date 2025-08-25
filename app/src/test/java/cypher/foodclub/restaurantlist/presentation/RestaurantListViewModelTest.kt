package cypher.foodclub.restaurantlist.presentation

import app.cash.turbine.test
import cypher.foodclub.core.data.network.models.RestaurantsListResponse.Restaurant
import cypher.foodclub.core.utils.customdispatcher.TestDispatcherProvider
import cypher.foodclub.core.utils.toRestaurantListDisplayModel
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel
import cypher.foodclub.restaurantlist.domain.usecases.GetRestaurantsListData
import cypher.foodclub.restaurantlist.domain.usecases.SearchRestaurants
import cypher.foodclub.restaurantlist.presentation.states.RestaurantsUiEvents
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RestaurantListViewModelTest {
    private val getRestaurantsList: GetRestaurantsListData = mockk(relaxed = true)
    private val searchRestaurants: SearchRestaurants = mockk(relaxed = true)
    private lateinit var viewModel: RestaurantListViewModel
    private val searchQuery = "italian"
    private val restaurantId = "123"
    private val testDispatcherProvider = TestDispatcherProvider()

    private fun simulateGetRestaurantsListSuccess(emptyList: Boolean = false) {
        coEvery { getRestaurantsList() } returns Result.success(if (emptyList) listOf() else generateDummyRestaurantsForTesting())
        viewModel = RestaurantListViewModel(testDispatcherProvider, getRestaurantsList, searchRestaurants)
    }

    private fun simulateGetRestaurantsListFailure() {
        coEvery { getRestaurantsList() } returns Result.failure(IllegalStateException())
        viewModel = RestaurantListViewModel(testDispatcherProvider, getRestaurantsList, searchRestaurants)
    }

    private fun simulateSearchRestaurantsWithResults() {
        every { searchRestaurants.invoke(searchQuery) } returns generateDummyRestaurantsForTesting().filter {
            it.name.lowercase().contains(searchQuery) || it.cuisines.any { it.lowercase() == searchQuery.lowercase() }
        }
    }

    private fun simulateSearchRestaurantsWithNoResults() {
        every { searchRestaurants.invoke(searchQuery) } returns listOf()
    }

    @Before
    fun setUp() {
        simulateSearchRestaurantsWithResults()
        simulateGetRestaurantsListSuccess()
    }


    @Test
    fun `getUiState successful fetch`() {
        assert(!viewModel.uiState.value.isLoading)
        assert(viewModel.uiState.value.displayedRestaurants.isNotEmpty())
    }

    @Test
    fun `getUiState fetch failure`() {
        simulateSearchRestaurantsWithResults()
        simulateGetRestaurantsListFailure()
        assert(!viewModel.uiState.value.isLoading)
        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `getUiState no restaurants fetched`() {
        simulateSearchRestaurantsWithResults()
        simulateGetRestaurantsListSuccess(emptyList = true)
        assert(viewModel.uiState.value.displayedRestaurants.isEmpty())
        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `getUiEvents onRestaurantClicked event emission`() = runTest {
        viewModel.uiEvents.test {
            viewModel.onRestaurantClicked(restaurantId)
            val event = awaitItem()
            assert(event is RestaurantsUiEvents.OpenRestaurantDetails)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onRestaurantClicked with valid ID`() = runTest {
        viewModel.uiEvents.test {
            viewModel.onRestaurantClicked(restaurantId)
            val event = awaitItem()
            assert(event is RestaurantsUiEvents.OpenRestaurantDetails)
            assert((event as RestaurantsUiEvents.OpenRestaurantDetails).id == restaurantId)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `searchStoredRestaurants with valid query`() = runTest {
        viewModel.uiState.test {
            viewModel.searchStoredRestaurants(searchQuery)
            awaitItem()
            awaitItem()

            val dataEvent = awaitItem()
            assert(dataEvent.currentSearchQuery == searchQuery)
            assert(dataEvent.displayedRestaurants.size == generateDummyRestaurantsForTesting().filter {
                it.name.lowercase()
                    .contains(searchQuery) || it.cuisines.any { it.lowercase() == searchQuery.lowercase() }
            }.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchStoredRestaurants with empty query`() = runTest {
        viewModel.uiState.test {
            viewModel.searchStoredRestaurants("")
            awaitItem()
            awaitItem()
            val dataEvent = awaitItem()
            assert(dataEvent.currentSearchQuery == "")
            assert(dataEvent.displayedRestaurants.size == generateDummyRestaurantsForTesting().size)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `searchStoredRestaurants no matching results`()  = runTest{
        simulateSearchRestaurantsWithNoResults()
        simulateGetRestaurantsListSuccess()
        viewModel.uiState.test {
            val randomQuery = "random query"
            viewModel.searchStoredRestaurants(randomQuery)
            awaitItem()
            awaitItem()
            val dataEvent = awaitItem()
            assert(dataEvent.currentSearchQuery == randomQuery)
            assert(dataEvent.displayedRestaurants.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    fun generateDummyRestaurantsForTesting(): List<RestaurantListDisplayModel> {
        val dummyDealsSet1 = listOf(
            Restaurant.Deal(
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
            Restaurant.Deal(
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
            Restaurant.Deal(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
            Restaurant(
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
        ).toRestaurantListDisplayModel()
    }
}