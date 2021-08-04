package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Hour(
    override val time: String,
    @Json(name="temp_c")
    override val tempC: String,
    @Json(name="is_day")
    override val isDay: Int,
    override val condition: Condition,
    @Json(name="wind_mph")
    override val windMph: Double,
    @Json(name="wind_degree")
    override val windDegree: Int,
    @Json(name="wind_dir")
    override val windDir: String,
    @Json(name="pressure_mb")
    override val pressureMb: Double,
    @Json(name="precip_mm")
    override val precipMM: Double,
    override val humidity: Int,
    @Json(name="feelslike_c")
    override val feelsLikeC: Double,
    @Json(name="windchill_c")
    override val windChill: Double,
    @Json(name="heatindex_c")
    override val heatIndexC: Double,
    @Json(name="dewpoint_c")
    override val dewPointC: Double,
    @Json(name="will_it_rain")
    override val willItRain: Int,
    @Json(name="chance_of_snow")
    override val chanceOfSnow: String,
    @Json(name="chance_of_rain")
    override val chanceOfRain: String,
    @Json(name="vis_km")
    override val visibilityKm: Double,
    @Json(name="gust_mph")
    override val gustMph: Double
):BaseHour
