package cypher.foodclub.restaurantlist.domain.models

data class BestDeal(
    val bestDiscount: String,
    val isDealDineInOnly: Boolean,
    val isLimitedTimeDeal: Boolean,
    val validTill: String
)