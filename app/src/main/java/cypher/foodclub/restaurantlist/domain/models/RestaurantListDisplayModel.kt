package cypher.foodclub.restaurantlist.domain.models

data class RestaurantListDisplayModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val cuisines: String,
    val address: String,
    val availableOptions: String,
    val bestDeal: BestDeal
)