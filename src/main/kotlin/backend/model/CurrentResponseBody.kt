package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class CurrentResponseBody(@Json(name="location")val location: Location,
@Json(name="current")val current: Current){

}
