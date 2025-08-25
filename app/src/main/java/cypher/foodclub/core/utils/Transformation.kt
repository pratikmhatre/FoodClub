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
        cuisines = getCuisinesText(cuisines),
        address = "$address, $suburb",
        workHours = "$opensAt - $closesAt",
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
            isMultipleQuantityLeft = it.qtyLeft.toInt() > 1,
            isLimitedTime = (it.start ?: it.opensAt).isNullOrBlank().not()
        )
    }
}

fun List<Restaurant>.toRestaurantListDisplayModel(): List<RestaurantListDisplayModel> {
    return this.map {
        RestaurantListDisplayModel(
            id = it.id,
            name = it.name,
            imageUrl = it.imageLink,
            cuisines = getCuisinesText(it.cuisines),
            address = "${it.address}, ${it.suburb}",
            availableOptions = "Dine-In | Takeaway | Order Online",
            bestDeal = getBestDealData(it.deals),
        )
    }
}

private fun getCuisinesText(cuisines: List<String>): String {
    val builder = StringBuilder()
    cuisines.forEachIndexed { index, cuisine ->
        builder.append(cuisine)
        if (index != cuisines.lastIndex) builder.append(", ")
    }
    return builder.toString()
}

private fun getBestDealData(dealsList: List<Restaurant.Deal>): BestDeal {
    val bestDeal = dealsList.maxBy { it.discount.toDouble() }
    return BestDeal(
        bestDiscount = bestDeal.discount,
        isDealDineInOnly = bestDeal.dineIn == "true",
        isLimitedTimeDeal = (bestDeal.opensAt ?: bestDeal.start).isNullOrBlank().not(),
        validTill = bestDeal.closesAt ?: bestDeal.end ?: ""
    )
}