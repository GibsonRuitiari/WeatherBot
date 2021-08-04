package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Current(
    @Json(name="last_updated")
    override val lastUpdated: String,
    @Json(name="temp_c")
    override val tempC: Double,
    @Json(name="temp_f")
    override val tempF: Double,
    @Json(name="feelslike_c")
    override val feelsLikeC: Double,
    @Json(name="feelslike_f")
    override val feelsLikeF: Double,
    override val condition: Condition,
    @Json(name="wind_mph")
    override val windMph: Double,
    @Json(name="wind_kph")
    override val windKph: Double,
    @Json(name="wind_dir")
    override val windDir: String,
    @Json(name="pressure_mb")
    override val pressureMb: Double,
    @Json(name="precip_mm")
    override val precipMM: Double,
    @Json(name="humidity")
    override val humidity: Int,
    override val cloud: Int,
    @Json(name="is_day")
    override val isDay: Int,
    @Json(name="gust_mph")
    override val gustMph: Double
):BaseRealTime
