package backend.network

import backend.model.LocationModel
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException



suspend fun Call.awaitBody(): ResponseBody {
    return suspendCancellableCoroutine {
        it.invokeOnCancellation { cancel() }
        enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) it.resume(response.body!!)
                else {
                    it.resumeWithException(IOException("Call unsuccessful Reason: ${response.message} ${response.code}"))
                }
            }
        })
    }
}

fun getLocationCoordinates(result:(locationModel:LocationModel?,errorMessage:String?)->Unit){
    val client = OkHttpClient()
    val moshi = Moshi.Builder().build()
    val adapter = moshi.adapter(LocationModel::class.java)
    val request = Request.Builder().url("https://freegeoip.app/json/")
        .get()
        .addHeader("accept","application/json")
        .addHeader("content-type","application/json").build()
   val response= client.newCall(request).execute()
    if (response.isSuccessful){
        response.body?.use {
            val reader = JsonReader.of(it.source())
            val location =adapter.fromJson(reader)
            result(location,null)
        }
    }else result(null,response.message)
}
