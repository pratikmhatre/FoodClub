package cypher.foodclub.restaurantdetails.domain.models

data class RestaurantDetailsModel(
    val name: String,
    val imageUrl: String,
    val cuisines: List<String>,
    val address: String,
    val workHours: String,
    val deals: List<Deal>
)