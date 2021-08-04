import backend.network.getLocationCoordinates
import backend.network.weather
import ch.qos.logback.core.subst.Token
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.location
import com.github.kotlintelegrambot.entities.ChatAction
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlinx.coroutines.runBlocking
import tokens.TelegramApiType
import tokens.TokenReader
import tokens.WeatherApiType
import utilities.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.system.exitProcess


fun main() {
  botStartUp()
}

private fun botStartUp(){
    val startMessage = """
                    Hey There😄.I will be sending you weather ☔ reports based on your location upon your request. Sit tight and use me😌
                    To start press Weather Report button or send me a location pin.
                """.trimIndent()
    val helpMessage = """
         WeatherMan 👻
     
         Provides weather reports i.e rain, temperature,pressure and wind speed.
         This bots automatically detects your location based on your ip and uses it.
         So you don't have to provide it your location..cool right? 🤓
         
         Commands:
         /weatherfull - gives you forecast data based on your current location
         /weatherhelp - to view help/instructions page
         /stop - stops the bot 
         
         You can also send me a location pin instead and I'll still work my magic.
         
         Format
         The report consists of summary and detailed report.
         The summary consists of data from 00:00am,3:00am,6:00am upto 8:00pm (intervals of 3hrs)
         The icons represent the weather description at the time eg 🌤 means cloudy/partly cloudy.
         Contact @nerd_loco for any questions,comments etc.
         v1.0
     """.trimIndent()

    val mainMenuButtons = InlineKeyboardMarkup.create(
        listOf(InlineKeyboardButton.CallbackData(" Weather Report ", callbackData = "weatherReportCallback")),
        listOf(InlineKeyboardButton.CallbackData("Help", callbackData = "helpCallback"))
    )
     bot {
        token = TokenReader.provideToken(TelegramApiType)
        dispatch {
            /**
             * Commands
             */
            command("start") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = startMessage,
                    replyMarkup = mainMenuButtons).logTelegramMessageResponse()

            }
            command("weatherhelp") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = helpMessage).logTelegramMessageResponse()
            }
            command("weatherfull"){
                val chatId = ChatId.fromId(message.chat.id)
                bot.sendLoadingMessage(chatId,editText = false,null, null)
                getLocationCoordinates { locationModel, errorMessage ->
                    when{
                        errorMessage!=null->{
                            bot.sendErrorMessage(false,chatId = chatId,messageId = null,inlineMenu = null)
                        }
                        locationModel!=null->{
                            bot.sendWeatherReport(chatId = ChatId.fromId(message.chat.id), response = getFormattedWeatherDetails("${locationModel.latitude},${locationModel.longitude}"))
                        }
                        else->{
                            Logger.provideLogger().info("an unknown error occured :-)")
                            bot.sendErrorMessage(false,chatId = chatId,messageId = null,inlineMenu = null)
                        }
                    }

                }
            }
            command("stop"){
                bot.sendChatAction(ChatId.fromId(message.chat.id),action = ChatAction.TYPING)
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id),text = "Sorry to see you go ..bye \uD83D\uDC4B").logTelegramMessageResponse()
                bot.stopPolling()
                exitProcess(1)
            }
            /**
             * Callback queries
             */
            callbackQuery(callbackData = "helpCallback") {
                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                bot.editMessageText(
                    chatId = ChatId.fromId(chatId), text = helpMessage, messageId = callbackQuery.message!!.messageId, replyMarkup = mainMenuButtons
                ).logTelegramMessageResponse()
            }

            callbackQuery(callbackData= "weatherReportCallback") {
                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                val messageId = callbackQuery.message!!.messageId
                bot.sendLoadingMessage(id = ChatId.fromId(chatId),editText = true, messageId = messageId, inlineMenu = mainMenuButtons)
                bot.sendWeatherReport(ChatId.fromId(chatId),messageId = messageId,inlineMenu = mainMenuButtons)
            }
            /**
             * Messages when the user sends a location pin
             */
            location {
                bot.sendLoadingMessage(ChatId.fromId(message.chat.id))
                val locationPin ="${location.latitude},${location.longitude}"
                Logger.provideLogger().info("received location pin is: $locationPin")
                bot.sendWeatherReport(chatId = ChatId.fromId(message.chat.id), response = getFormattedWeatherDetails(locationPin))
            }

        }

    }.startPolling()

}



private fun Bot.sendWeatherReport(chatId: ChatId, messageId: Long, inlineMenu: ReplyMarkup?){
    val errorText ="Ooops \uD83D\uDC36 ! Failed to determine your location, don't fret let's try again..."
    getLocationCoordinates { locationModel, errorMessage ->
        when{
            !errorMessage.isNullOrEmpty()->{
                Logger.provideLogger().error("error occurred; reason:$errorMessage")
                editMessageText(
                    chatId = chatId,
                    text = errorText,
                    messageId = messageId,
                    replyMarkup = inlineMenu).logTelegramMessageResponse()
            }
            locationModel!=null->{
                Logger.provideLogger().info("received the weather information successfully")
                Logger.provideLogger().info("location model:${locationModel.city}")
                editMessageText(
                    chatId = chatId,
                    text = "Weather report for ${locationModel.city}, ${locationModel.countyName} \uD83D\uDCCD",
                    messageId = messageId,
                    replyMarkup = inlineMenu).logTelegramMessageResponse()
                sendChatAction(chatId, action = ChatAction.TYPING)

                val latLong = "${locationModel.latitude},${locationModel.longitude}"
                val response =getFormattedWeatherDetails(latLong)
                Logger.provideLogger().info("formatted weather response:$response")
                editMessageText(
                    chatId =chatId,
                    text = response,
                    messageId = messageId,
                    replyMarkup = inlineMenu).logTelegramMessageResponse()
            }
            else->{
                Logger.provideLogger().error("unknown error ")
                sendErrorMessage(true,chatId, inlineMenu = inlineMenu,messageId = messageId)
            }
        }
    }

}


private fun getFormattedWeatherDetails(latLong_: String): String {
    val formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    var responseBody =""
    runBlocking {
        val response= weather {
            token= TokenReader.provideToken(WeatherApiType)
            lattLong=latLong_ }
            .getWeatherForecastData()
        if (!isResponseNull(response)){

            val condition = response.current.condition.text
            val time_=LocalDateTime.parse(response.location.localTime,formatter)


            val humidity=response.forecast.forecastDay[0].day.avgHumidity
            val speed = response.forecast.forecastDay[0].day.maxWindMph

            val conditionEmoji = condition.emojiProvider()
            // sun set
            val sunRise = response.forecast.forecastDay[0].astro.sunrise
            val sunSet = response.forecast.forecastDay[0].astro.sunset
            //moon
            val moonPhaseEmoji = response.forecast.forecastDay[0].astro.moonPhase.moonPhaseEmoji()
            val moonPhase = response.forecast.forecastDay[0].astro.moonPhase
            val moonRise = response.forecast.forecastDay[0].astro.moonRise
            val moonSet = response.forecast.forecastDay[0].astro.moonSet

            // temp
            val daysHigh = response.forecast.forecastDay[0].day.maxTempC
            val daysLow = response.forecast.forecastDay[0].day.minTempF
            val avgTemp =response.forecast.forecastDay[0].day.avgTempC
            val feelsLikeTmp = response.current.feelsLikeC
            val chanceOfRain =response.forecast.forecastDay[0].hour.first {
                val date = LocalTime.parse(it.time,formatter)
                LocalTime.now().hour.compareTo(date.hour)==0
            }.chanceOfRain
            val daysSummaryCondition = response.forecast.forecastDay[0].day.condition.text

            val summaryText = response.forecast.forecastDay[0].hour.filter {
                val date = LocalTime.parse(it.time, formatter)
                val hourList = listOf(0, 3, 6, 9, 12, 15, 18, 21)
                date.hour in hourList
            }.map { it.condition.text }
            val summaryHoursEmoji= summaryText.joinToString(separator = "->") { it.emojiProvider() }

            responseBody = """   
        updated as of ${time_.format( DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}
        
        $conditionEmoji $condition
      
       Summary
      
       00:00 AM to 8:00 PM interval
       
       $summaryHoursEmoji
       ${summaryText.joinToString(separator = "->")}
      
       Day Details
       
       Expect $daysSummaryCondition sometime during the day
       Chance of raining $chanceOfRain% 🌧
       
          Temperature
         The day's high will be  $daysHigh°C 
         and the low will be  $daysLow°C with an average of $avgTemp°C but 
         it feels like $feelsLikeTmp°C.
     
         
           Sunrise
          🌞 $sunRise
           Sunset
          🌞 $sunSet
          
           Moon Phase
          $moonPhaseEmoji $moonPhase
          
           Moonrise
          🌙 $moonRise
           Moonset
          🌙 $moonSet
          
           Humidity
          💦 $humidity%
          
           Wind speed/direction
          🌬 $speed mph/h 
     """.trimIndent()
        }else{
            responseBody="Yikes, failed to get weather data..please make sure you are connected to the internet and try again later"
        }
    }
    return responseBody
}