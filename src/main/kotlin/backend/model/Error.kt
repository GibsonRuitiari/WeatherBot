package backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Error(val code:Int, val message:String)
