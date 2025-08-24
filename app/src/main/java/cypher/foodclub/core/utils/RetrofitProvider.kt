package cypher.foodclub.core.utils

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

object RetrofitInstance {
    private val json = Json {
        ignoreUnknownKeys = true // avoids crashes if API sends extra fields
        isLenient = true
    }

    private val contentType = "application/json".toMediaType()

    private fun createRetrofit(baseUrl : String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    fun <T> create(baseUrl : String, service: Class<T>): T {
        return createRetrofit(baseUrl).create(service)
    }
}
