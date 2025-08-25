package cypher.foodclub.core.utils

import cypher.foodclub.core.data.network.models.RestaurantsListResponse.Restaurant
import cypher.foodclub.restaurantdetails.domain.models.Deal
import cypher.foodclub.restaurantdetails.domain.models.RestaurantDetailsModel
import cypher.foodclub.restaurantlist.domain.models.BestDeal
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel


fun Restaurant.toRestaurantDetailsModel(): RestaurantDetailsModel {
    return RestaurantDetailsModel(
        name = this.name,
        imageUrl = this.imageLink,
        cuisines = cuisines,
        address = "$address, $suburb",
        workHours = "",
        deals = deals.getSortedDeals()
    )
}

private fun List<Restaurant.Deal>.getSortedDeals(): List<Deal> {
    return this.sortedByDescending {
        it.discount.toDouble()
    }.map {
        Deal(
            discount = it.discount,
            effectiveBetween = "${it.start ?: it.opensAt} - ${it.end ?: it.closesAt}",
            quantityLeft = it.qtyLeft,
            isLimitedTime = (it.start ?: it.opensAt).isNullOrBlank().not()
        )
    }
}

fun List<Restaurant>.toRestaurantListDisplayModel(): List<RestaurantListDisplayModel> {
    return this.map {
        RestaurantListDisplayModel(
            name = it.name,
            imageUrl = it.imageLink,
            cuisines = it.cuisines,
            address = "${it.address}, ${it.suburb}",
            bestDeal = getBestDealData(it.deals),
        )
    }
}

private fun getBestDealData(dealsList: List<Restaurant.Deal>): BestDeal {
    val bestDeal = dealsList.minByOrNull { it.discount.toDouble() }!!
    return BestDeal(
        bestDiscount = bestDeal.discount,
        isDealDineInOnly = bestDeal.dineIn == "true",
        isLimitedTimeDeal = (bestDeal.opensAt ?: bestDeal.start).isNullOrBlank().not(),
        validTill = bestDeal.closesAt ?: ""
    )
}