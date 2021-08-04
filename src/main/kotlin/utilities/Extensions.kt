package utilities

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatAction
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.network.Response
import com.github.kotlintelegrambot.network.fold
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun<T> isResponseNull(body:T?):Boolean{
    contract {
        returns(false) implies(body!=null)
    }
    return body==null
}
/*
String extensions
 */
 fun String.emojiProvider():String{
    return when(this){
        "Clear"->"\uD83C\uDF1E"
        "Cloudy","Partly cloudy"->"☁"
        "Mist"->"\uD83C\uDF2B"
        "Sunny"->"☀"
        "Blizzard"->"\uD83C\uDF2A"
        "Fog","Freezing fog"->"\uD83D\uDCA8"
        "Drizzle","Patchy light drizzle"->"\uD83C\uDF28"
        "Heavy rain", "Heavy rain at times"->"\uD83C\uDF28"
        "Light Rain", "Moderate rain","Light rain shower","Patchy rain possible"->"\uD83C\uDF26"
        "Light sleet","Moderate or heavy sleet"->"⛈"
        "Snow", "Heavy snow","Light snow","Patchy heavy snow","Light snow showers","Patchy snow possible"->"❄"
        else->"\uD83C\uDF15"
    }
}

 fun String.moonPhaseEmoji():String{
    return when(this){
        "Waxing Crescent"->"\uD83C\uDF12"
        "Waning Crescent"->"\uD83C\uDF18"
        "Waning Gibbous"->"\uD83C\uDF16"
        "Waxing Gibbous"->"\uD83C\uDF14"
        "Fullmoon"->"\uD83C\uDF15"
        else->"\uD83C\uDF18"
    }
}


 fun Pair<retrofit2.Response<Response<Message>?>?, Exception?>.logTelegramMessageResponse() {
    this.fold({
        val messageLog = if (it?.ok==true) "success getting the message" else "the following error occurred: ${it?.errorDescription}"
        Logger.provideLogger().info(messageLog)
    }, {
        Logger.provideLogger().error("the following error occurred: ${it.errorBody?.string()}")
    })
}

/**
 * Bot extensions
 */
 fun Bot.sendLoadingMessage(id: ChatId, editText:Boolean=false, messageId: Long?=null, inlineMenu: ReplyMarkup?=null){
    val loadingMessage ="Working \uD83E\uDD2F...give me some minutes"
    sendChatAction(id, action = ChatAction.TYPING)
    if (editText){
        checkNotNull(messageId){"Message id provided is null :$messageId"}
        editMessageText(chatId = id, text =loadingMessage,messageId = messageId,replyMarkup = inlineMenu).logTelegramMessageResponse()
    }else sendMessage(chatId = id, text = loadingMessage,replyMarkup = inlineMenu).logTelegramMessageResponse()

}

 fun Bot.sendWeatherReport(chatId: ChatId, inlineMenu: ReplyMarkup?=null, response: String){
    Logger.provideLogger().info("received response:$response")
    sendMessage(chatId,text = response,replyMarkup = inlineMenu)
}

 fun Bot.sendErrorMessage(editText: Boolean=false, chatId: ChatId,messageId: Long?=null, inlineMenu: ReplyMarkup?){
    val errorText ="Ooops \uD83D\uDC36 ! Failed to determine your location, don't fret let's try again..."
    if (editText){
        checkNotNull(messageId){"Message id cannot be null:$messageId"}
        editMessageText(chatId,messageId = messageId,replyMarkup = inlineMenu,text = errorText).logTelegramMessageResponse()
    }
    else sendMessage(chatId = chatId,text = errorText, replyMarkup = inlineMenu).logTelegramMessageResponse()
}
