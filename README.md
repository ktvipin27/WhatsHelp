# WhatsHelp

_**[WhatsHelp](https://play.google.com/store/apps/details?id=com.whatshelp)**_ is an android
application which helps to start WhatsApp chat with numbers that are not saved in contact list.


## Features

* Support for Whatsapp & WhatsApp Business.
* Option to select number from call history.
* Detects numbers copied from other applications.
* Generate WhatsApp chat link.
* Saves recently opened numbers for reusing.
* Dark theme support.
* Auto select country while pasting number with country code.
* Number formatting according to country.
* Pre written messages for sending Quick messages.

## Screenshots

<img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.3/1.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.1/L2.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.3/2.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.3/3.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.1/L4.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.1/L5.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.1/L6.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.2/3.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.3/4.webp?raw=true" width="120" height="240" /> <img src="https://github.com/ktvipin27/WhatsHelp/blob/master/screenshots/v1.0.1/L9.webp?raw=true" width="120" height="240" />

## About this project

_**WhatsHelp application**_ is built with _Modern Android application development_ tools and libraries.
Aim of this project is to showcase Good practice implementation of Android application development with proper architecture design.

The codebase focuses on following key things:

- [x] Single Activity Design
- [x] MVVM Architecture with Jetpack libraries
- [x] Clean and Simple Material UI

## Built with

- [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) - Recommended architecture for building robust, production-quality apps.
- [Kotlin](https://kotlinlang.org/) - Official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - A coroutine is a concurrency design pattern that can be used on Android to simplify code that executes asynchronously.
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [DataBinding](https://developer.android.com/topic/libraries/data-binding) - A support library that allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
  - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - WorkManager is an API that makes it easy to schedule deferrable, asynchronous tasks that are expected to run even if the app exits or the device restarts.
  - [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
- [Material Components](https://github.com/material-components/material-components-android) -  Help developers execute Material Design, enable a reliable development workflow to build beautiful and functional Android apps.
- [Constraint Layout](https://developer.android.com/training/constraint-layout) - Allows to create large and complex layouts with a flat view hierarchy (no nested view groups).
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection.
- [Gson](https://github.com/google/gson) - A Java library that can be used to convert Java Objects into their JSON representation annd vise versa.
- [Firebase](https://firebase.google.com/) - Google's mobile platform that helps to quickly develop high-quality apps.
  - [Firebase-Crashlytics](https://firebase.google.com/products/crashlytics/) - A lightweight,
    realtime crash reporter.
  - [Firebase-Analytics](https://firebase.google.com/firebase/analytics) - Google Analytics helps
    you measure the app metrics that matter to your business.
  - [Firebase-Realtime-Database](https://firebase.google.com/products/realtime-database) - A
    cloud-hosted NoSQL database that lets you store and sync data between your users in realtime.
- [CountryCodePicker](https://github.com/hbb20/CountryCodePickerProject) - Provides an easy way to search and select country or country phone code for the telephone number.
- [libphonenumber-android](https://github.com/MichaelRocks/libphonenumber-android) - Android port of
  Google's [libphonenumber](https://github.com/googlei18n/libphonenumber).
- [Peko](https://github.com/deva666/Peko) - Android Permissions with Kotlin Coroutines or LiveData.

## License

    Copyright 2021 Vipin KT

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
