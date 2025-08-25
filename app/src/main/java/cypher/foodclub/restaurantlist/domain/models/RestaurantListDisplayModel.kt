package cypher.foodclub.restaurantlist.domain.models

data class RestaurantListDisplayModel(
    val name: String,
    val imageUrl: String,
    val cuisines: List<String>,
    val address: String,
    val dineInAvailable: Boolean = false,
    val takeAwayAvailable: Boolean = false,
    val bestDeal: BestDeal
)