package by.bstu.vs.stpms.lr13.ui

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.lr13.R
import by.bstu.vs.stpms.lr13.data.model.Article
import by.bstu.vs.stpms.lr13.data.model.MeasureUnits
import by.bstu.vs.stpms.lr13.data.model.Weather
import by.bstu.vs.stpms.lr13.data.repository.NewsRepository
import by.bstu.vs.stpms.lr13.data.repository.WeatherRepository
import by.bstu.vs.stpms.lr13.data.util.units
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = "MainViewModel"

    var newsRepository = NewsRepository()
    var weatherRepository = WeatherRepository()

    val isRefreshing = MutableStateFlow(false)

    var weather by mutableStateOf<Weather?>(null)
    var news: List<Article> by mutableStateOf(listOf())

    val weatherKey: String = application.getString(R.string.weather_key)
    val newsKey: String = application.getString(R.string.news_key)

    fun fetchData(location: Location) {
        val locale = Locale.getDefault()
        Log.d(TAG, "fetchData: locale $locale")
        getWeather(
            location = location,
            language = locale.language,
            units = locale.units
        )
        getArticles(locale)
    }

    private fun getWeather(location: Location, language: String, units: MeasureUnits) {
        viewModelScope.launch {
            weather = null
            weather = weatherRepository.getWeather(
                location = location,
                appId = weatherKey,
                units = units,
                language = language
            )
            Log.i(TAG, "getWeather: for location: $location, " +
                    "language: $language, measure units: $units, received weather: $weather")
        }
    }

    private fun getArticles(locale: Locale) {
        viewModelScope.launch {
            news = listOf()
            news = newsRepository.getNews(
                appId = newsKey,
                country = locale.country
            )
            if (news.isEmpty()) {
                val language = locale.language
                Log.w(TAG, "getArticles: API returns empty list for country: ${locale.country}" +
                        ", trying to get articles by locale language $language.")
                news = newsRepository.getNews(
                    appId = newsKey,
                    country = language
                )
            }
            Log.i(TAG, "getArticles: for locale $locale received news of size ${news.size}")
        }
    }

}