package backend.network

import backend.model.CurrentResponseBody
import backend.model.ForeCastResponseBody
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

fun weather(body:WeatherExtension.Builder.()->Unit):WeatherExtension = WeatherExtension.Builder().build(body)
class WeatherExtension private constructor(
     private val api:String,
     private val lattLong:String?,
timeout:Long=30,
timeoutUnit:TimeUnit=TimeUnit.SECONDS) {
    private val client :OkHttpClient = OkHttpClient.Builder()
        .readTimeout(timeout, timeoutUnit)
        .callTimeout(timeout,timeoutUnit)
        .build()
    private val foreCastUrl:String ="http://api.weatherapi.com/v1/forecast.json"
    private val currentUrl:String ="http://api.weatherapi.com/v1/current.json"
    private val moshi = Moshi.Builder().build()
    private val foreCastAdapter= moshi.adapter(ForeCastResponseBody::class.java)
    private val currentAdapter = moshi.adapter(CurrentResponseBody::class.java)
    class Builder{
        lateinit var token:String
        lateinit var lattLong:String//"48.8567,2.3508" // not a must but if you are going to use make sure the coordinates are right
        fun build(body: Builder.()->Unit):WeatherExtension{
            body()
            return WeatherExtension(api = token,lattLong )
        }
    }
    suspend fun getWeatherForecastData():ForeCastResponseBody?{
        val baseUrl = foreCastUrl.toHttpUrl().buildFullUrl()
        val request = Request.Builder()
            .url(baseUrl)
            .build()
        val response=client.newCall(request).awaitBody()
        return response.source().use {
            val json = JsonReader.of(it)
            return@use foreCastAdapter.fromJson(json)
        }

    }
    suspend fun getWeatherUpdates():CurrentResponseBody?{
        val fullUlr:HttpUrl =currentUrl.toHttpUrl().buildFullUrl()
        val request = Request.Builder()
            .url(fullUlr)
            .build()
        val response=client.newCall(request).awaitBody()
        return response.source().use {
            val json = JsonReader.of(it)
            return@use currentAdapter.fromJson(json)
        }
    }
    private fun HttpUrl.buildFullUrl():HttpUrl{
        return this.newBuilder().addQueryParameter("key", api)
            .addQueryParameter("q",lattLong).build()

    }

}

