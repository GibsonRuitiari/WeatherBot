package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class ForeCast(
    @Json(name="forecastday")
    override val forecastDay: List<ForeCastDay>):BaseForeCast
