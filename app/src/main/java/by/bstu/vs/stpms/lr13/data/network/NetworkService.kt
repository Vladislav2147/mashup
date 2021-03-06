package by.bstu.vs.stpms.lr13.data.network

import by.bstu.vs.stpms.lr13.data.model.Weather
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Singleton that provides [ApiNews] and [ApiWeather] implementations
 */
object NetworkService {

    private const val BASE_NEWS_URL = "https://newsapi.org/"
    private const val BASE_WEATHER_URL = "https://api.openweathermap.org/"

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client: OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    private val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

    /**
     * Provides [ApiNews] implementation
     * @return [ApiNews]
     */
    fun newsService(): ApiNews {
        return Retrofit.Builder()
                .baseUrl(BASE_NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(ApiNews::class.java)
    }

    /**
     * Provides [ApiWeather] implementation
     * @return [ApiWeather]
     */
    fun weatherService(): ApiWeather {
        return Retrofit.Builder()
                .baseUrl(BASE_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiWeather::class.java)
    }

    /**
     * returns [Weather] image url by its [code]
     */
    fun getImageUrlByCode(code: String) = "https://openweathermap.org/img/wn/${code}@2x.png"
}