package utilities

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logger {
    fun provideLogger():Logger{
      return  LoggerFactory.getLogger("com.gibsoncodes.weatherbot")
    }
}