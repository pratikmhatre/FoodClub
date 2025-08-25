package cypher.foodclub.restaurantdetails.domain.models

data class RestaurantDetailsModel(
    val name: String,
    val imageUrl: String,
    val cuisines: String,
    val address: String,
    val workHours: String,
    val deals: List<Deal>
)