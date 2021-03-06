package by.bstu.vs.stpms.lr13.data

import by.bstu.vs.stpms.lr13.data.model.Article
import by.bstu.vs.stpms.lr13.data.model.MeasureUnits
import by.bstu.vs.stpms.lr13.data.model.Weather
import by.bstu.vs.stpms.lr13.data.network.NetworkService
import by.bstu.vs.stpms.lr13.data.network.model.NewsDto
import by.bstu.vs.stpms.lr13.data.network.model.WeatherDto
import by.bstu.vs.stpms.lr13.data.util.round
import kotlin.math.roundToInt

/**
 * Maps [WeatherDto] to [Weather]
 */
fun WeatherDto.toWeather(units: MeasureUnits): Weather {
    return Weather(
        city = name,
        description = weather.first().description,
        temperature = main.temperature.toDouble(),
        humidity = main.humidity.toInt(),
        windSpeed = wind.speed.toDouble(),
        measureUnits = units,
        windDirectionDegrees = wind.deg.toDouble().roundToInt(),
        icon = this.weather.first().icon.let { NetworkService.getImageUrlByCode(it) }
    )
}

/**
 * Maps [NewsDto] to list of [Article]
 */
fun NewsDto.toArticleList(): List<Article> {
    return this.articles.map {
        Article(
            title = it.title,
            link = it.link,
            publishedAt = it.publishedAt,
            imageUrl = it.imageUrl ?: ""
        )
    }
}
