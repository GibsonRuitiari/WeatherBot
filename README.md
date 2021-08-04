Weather Bot 🤖
---------

A telegram bot 🤖, written in kotlin, which provides weather reports based on your location.

Tech Stack
--------
* Kotlin 
* Kotlin Serialization - for data serialization 
* Kotlin Coroutines - kotlin concurrency library for asynchronous programming https://kotlinlang.org/docs/coroutines-overview.html
* Moshi - modern json library for kotlin https://github.com/square/moshi
* OkHttp - efficient http client for kotlin https://square.github.io/okhttp/
* Okio - modern I/O library for kotlin https://github.com/square/okio
* Kotlin Telegram Bot Api Wrapper https://github.com/kotlin-telegram-bot/kotlin-telegram-bot


Sample
--------
<img src="kotlin/screenshots/screenshot_1.jpg" height="375"/>

Building
--------

Prerequisites:

* JDK 8 or newer
* Gradle 
* Weather Api 
* Telegram bot token 

Add both the weather api and telegram bot token to the cred.properties before running the project.

License
=======

    Copyright 2021 Gibson Ruitiari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
