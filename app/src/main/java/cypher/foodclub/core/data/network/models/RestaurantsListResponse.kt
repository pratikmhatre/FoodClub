package cypher.foodclub.core.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantsListResponse(
    val restaurants: List<Restaurant>
) {
    @Serializable
    data class Restaurant(
        @SerialName("objectId")
        val id: String,
        @SerialName("address1")
        val address: String,
        @SerialName("close")
        val closesAt: String,
        val cuisines: List<String>,
        val deals: List<Deal>,
        val imageLink: String,
        val name: String,
        @SerialName("open")
        val opensAt: String,
        val suburb: String
    ) {
        @Serializable
        data class Deal(
            @SerialName("objectId")
            val id: String,
            val dineIn: String,
            val discount: String,
            val lightning: String,

            @SerialName("open")
            val opensAt: String?,
            @SerialName("close")
            val closesAt: String?,

            val qtyLeft: String,

            val end: String?,
            val start: String?
        )
    }
}