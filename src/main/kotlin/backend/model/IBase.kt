package backend.model

interface BaseForeCastResponseBody{
    val location:BaseLocation
    val current:BaseRealTime
    val forecast:BaseForeCast
}
/**
 * Contains current weather data
 * data is returned as current object
 */
interface BaseRealTime{
    val lastUpdated:String // local time when real time data was updated
    val tempC: Double
    val tempF:Double
    val feelsLikeC:Double
    val feelsLikeF:Double
    val condition:BaseCondition
    val windMph:Double // miles per hour
    val windKph:Double // kilometres
    val windDir:String // NSW
    val pressureMb:Double // millibars
    val precipMM:Double // precipitate in mm
    val humidity:Int
    val cloud:Int
    val isDay:Int //1 yes 0 no
    val gustMph:Double // wind gust
}
/**
 * Location data which is also returned together with the current/real time data
 */
interface BaseLocation{
    val name:String
    val region:String
    val country:String
    val lat:Double
    val lon:Double
    val tz_id:String
    val localTime:String
}
/**
 * forecast data returns upto 10 days forecast data as forecast object
 * forecast object contains astronomy data, the days weather forecast
 * and hourly interval weather information
 */
interface BaseForeCast{
    // includes forecastday object which contains all data
    val forecastDay:List<BaseForeCastDay> // size depends on the days if day is 1 then size is 1 etc
}

interface BaseForeCastDay{
    val date:String
    val day:BaseDay
    val astro:BaseAstro
    val hour:List<BaseHour> // from 00:00 - 23:00 eg
}
interface BaseDay{
    val maxTempC:Double
    val minTempF:Double
    val avgTempC:Double
    val maxWindMph:Double
    val totalPrecipMM:Double
    val avgVisibilityKm:Double
    val avgHumidity:Double
    val dailyChanceOfRain:String // percentage
    val dailyChanceOfSnow:String // percentage in string
    val condition:BaseCondition
}
interface BaseHour{
    val time:String
    val tempC:String
    val isDay:Int // 0 true
    val condition:BaseCondition
    val windMph:Double
    val windDegree:Int
    val windDir:String // eg ENE
    val pressureMb:Double
    val precipMM:Double
    val humidity:Int
    val feelsLikeC:Double
    val windChill:Double
    val heatIndexC:Double
    val dewPointC:Double
    val willItRain:Int // 1 no 0 yes
    val chanceOfSnow:String
    val chanceOfRain:String
    val visibilityKm:Double
    val gustMph:Double
}
interface BaseAstro{
    val sunrise:String
    val sunset:String // 8:48
    val moonRise:String // no moonrise
    val moonSet:String // 3:00 pm
    val moonPhase:String // eg waning crescent, new moon, first quarter,full moon, waning gibbous, waxing crescent

}

interface BaseCondition{
    val icon:String
    val code:Int
    val text:String
}