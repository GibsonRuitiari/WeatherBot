package backend.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class ForeCastDay(
    override val date: String,
    override val day: Day,
    override val astro: Astro,
    override val hour: List<Hour>
):BaseForeCastDay
