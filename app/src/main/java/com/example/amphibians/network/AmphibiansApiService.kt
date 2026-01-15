package com.example.amphibians.network

import com.example.amphibians.data.Amphibian
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory


private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

private val json = Json {
    ignoreUnknownKeys = true   // if find extra fields in JSON response, ignore them
}

/* how it works :
*  retrofit create an implementation of the API endpoints defined by the service interface.
* then addConverterFactory is used to specify how the JSON data should be converted to Kotlin objects
* using kotlinx.serialization.
*
* */
@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .build()

/*
* This is for - > defining the API endpoints.
* Here we have only one endpoint "amphibians" which returns a list of Amphibian objects.
* The suspend keyword indicates that this function is designed to be called from a coroutine,
* allowing for asynchronous network operations without blocking the main thread.
* */
interface AmphibiansApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): List<Amphibian>
}

/*
* This is for - > creating a singleton object to access the AmphibiansApiService.
* The service property is lazily initialized, meaning it will only be created when it is first
* accessed. This ensures that there is only one instance of the service throughout the app,
* promoting efficient resource usage.
* */
object AmphibiansApi {
    val service: AmphibiansApiService by lazy {
        retrofit.create(AmphibiansApiService::class.java)
    }
}