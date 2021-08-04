package backend.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Condition(override val icon: String, override val code: Int, override val text: String):BaseCondition
