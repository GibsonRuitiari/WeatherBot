package tokens

import java.io.FileInputStream
import java.util.*
import kotlin.io.path.Path

sealed class TypeToken
object WeatherApiType : TypeToken()
object TelegramApiType:TypeToken()

object TokenReader {
    private val  path = Path("${System.getProperty("user.dir")}/cred.properties")
    private val fis = FileInputStream(path.toFile())
    private  val properties = Properties().also {
        it.load(fis)
    }
    fun provideToken(typeToken:TypeToken):String{
       return when(typeToken){
            TelegramApiType-> {
                properties["botToken"] as String
            }
            WeatherApiType->{
               properties["weatherToken"] as String
            }
        }
    }

}