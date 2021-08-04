package backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
/**
 * {"ip":"197.232.61.194","country_code":"KE","country_name":"Kenya","region_code":"30",
 * "region_name":"Nairobi Province","city":"Nairobi",
 * "zip_code":"",
 * "time_zone":"Africa/Nairobi","latitude":-1.2841,"longitude":36.8155,"metro_code":0}
 */
@JsonClass(generateAdapter = true)
data class LocationModel(val ip:String,
                         @Json(name="country_name")
val countyName:String, @Json(name="region_name")val regionName:String, val city:String,
                         @Json(name="time_zone")val timeZone:String, val latitude:String,
                         val longitude:String)
