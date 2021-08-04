package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Day(
    @Json(name="maxtemp_c")
    override val maxTempC: Double,
    @Json(name="mintemp_c")
    override val minTempF: Double,
    @Json(name="avgtemp_c")
    override val avgTempC: Double,
    @Json(name="maxwind_mph")
    override val maxWindMph: Double,
    @Json(name="totalprecip_mm")
    override val totalPrecipMM: Double,
    @Json(name="avgvis_km")
    override val avgVisibilityKm: Double,
    @Json(name="avghumidity")
    override val avgHumidity: Double,
    @Json(name="daily_chance_of_rain")
    override val dailyChanceOfRain: String,
    @Json(name="daily_chance_of_snow")
    override val dailyChanceOfSnow: String,
    override val condition: Condition
):BaseDay
