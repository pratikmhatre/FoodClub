package cypher.foodclub.core.data.network.models

import com.google.gson.annotations.SerializedName


data class RestaurantsListResponse(
    val restaurants: List<Restaurant>
) {
    data class Restaurant(
        @SerializedName("objectId")
        val id: String,
        @SerializedName("address1")
        val address: String,
        @SerializedName("close")
        val closesAt: String,
        val cuisines: List<String>,
        val deals: List<Deal>,
        val imageLink: String,
        val name: String,
        @SerializedName("open")
        val opensAt: String,
        val suburb: String
    ) {
        data class Deal(
            @SerializedName("objectId")
            val id: String,
            val dineIn: String,
            val discount: String,
            val lightning: String,

            @SerializedName("open")
            val opensAt: String?,
            @SerializedName("close")
            val closesAt: String?,

            val qtyLeft: String,

            val end: String?,
            val start: String?
        )
    }
}