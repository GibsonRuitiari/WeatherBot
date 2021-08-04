package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Astro(
    override val sunrise: String,
    override val sunset: String,
    @Json(name="moonrise")
    override val moonRise: String,
    @Json(name="moonset")
    override val moonSet: String,
    @Json(name="moon_phase")
    override val moonPhase: String
):BaseAstro
