package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Location(
    override val name: String,
    override val region: String,
    override val lat: Double,
    override val lon: Double,
    override val tz_id: String,
    @Json(name="localtime")
    override val localTime: String, override val country: String
):BaseLocation
