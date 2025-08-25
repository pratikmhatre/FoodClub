package cypher.foodclub.core.utils

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun createRetrofit(baseUrl : String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(baseUrl : String, service: Class<T>): T {
        return createRetrofit(baseUrl).create(service)
    }
}
