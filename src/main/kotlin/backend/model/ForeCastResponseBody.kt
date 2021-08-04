package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * ForeCast response body
 * Params as query
 *          i) days  : no of days of weather forecast 1-10
 *          ii) aqi : yes/no -> get air quality // not impl
 *          iii) alerts: yes/no-> get weather alert data // not impl
 */
@JsonClass(generateAdapter = true)
data class ForeCastResponseBody(
    @Json(name="location") override val location:Location,
    override val current: Current,
    override val forecast: ForeCast
):BaseForeCastResponseBody
