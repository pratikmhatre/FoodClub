package cypher.foodclub.restaurantdetails.domain.models

data class Deal(
    val discount: String,
    val isLimitedTime: Boolean,
    val effectiveBetween: String,
    val quantityLeft: String
)