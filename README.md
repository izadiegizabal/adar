# Adar
<img src="https://i.imgur.com/nSbfDUM.png" alt="Adar App Icon" style="height:96px">

Adar -_branch in the basque language_- is the name of my little lil version of Monetree. Built using __Android Compose__ and following the generally recommended [architecture](https://developer.android.com/jetpack/guide). Hope you like it! 

## Understanding how I work

For this challenge I've tried to use very modern android development architecture and components, not only for showcasing that I can adapt to new framework and technologies but also for my own
experimentation and seeing what's next to come. It's been fun playing with colors and transitions!

For that, as the base for the UI I have used Jetpack Compose (with the State Hoisting pattern for the State Management) and tried to emulate Material You style without the dynamic color
functionality (since I don't own a Pixel to try it).

For the architecture side of things, I've tried to separate the things following a MVVM pattern, alongside a repository pattern (using Room for the local storage and Kotlinx's Serialization for the
JSON mapping) and a domain layer with use cases to provide an extra level of decoupling of the data.

Besides, I've used Hilt for the Dependency Injection and flow with coroutines for the concurrency.

I've been able to finish most user stories (except the 5th) so I would be adding that if I had more time. Besides, more importantly, I would improve the test cases, since I tried to squish in more features with the downside of worse code coverage.

In a similar note, had I have more time, I would polish the remove transaction feature since it seems a bit flimsy in the current state. 🏹 🐛

## No warnings

I've checked and fixed issues from the Android Studio "Inspect Code", the Lint kept giving me this error and I couldn't find the root of the issue in a timely matter:

```
izadi.egizabal adar % ./gradlew lint
> Task :app:kaptDebugKotlin FAILED

FAILURE: Build failed with an exception.

* What went wrong:
  Execution failed for task ':app:kaptDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask$KaptExecutionWorkAction
> java.lang.reflect.InvocationTargetException (no error message)
```

It seems related to some annotation processor (`kapt`), therefore I would assume that it is related to either Hilt or Room, but I after checking their usage, I couldn't find what could be causing the
issue.

## Testing
I've tried unit testing as much as the time allowed. Mainly focused on the data manipulation part (repository / use cases / view model).

<img src="https://i.imgur.com/qzRR8x2.png" alt="UI Testing Screenshot" style="height:156px">

For integration tests, I didn't have much time, but I wrote a couple of sample UI tests to try testing Jetpack Compose UI.

<img src="https://i.imgur.com/B0DIDUX.png" alt="UI Testing Screenshot" style="height:156px">


## Accessibility

High contrast colours + content descriptions have been used thoughout the app. Touch targets are big and the interface is easy to use. The accessibility has been validated with Google's Accessibility
Scanner (no issues were found) for both the light and dark themes.

<img src="https://i.imgur.com/CLxBm3V.jpg" alt="Accessibility Scanner Screenshot" style="width:256px">

## Results

- Light/dark mode 🌞 🌚
- Android compose 🤖
- MVVM + Repository pattern ✨
- Local mode 🛫
- Coroutines + Kotlin ⚡️❤️
- Hilt for DI 💉
- Unit testing with JUnit + Mockito 🧪 *(not as thorough as I'd have liked due to the time limit 😅)*

|  Light 🌞 | Dark 🌚 |
| ------------- | ------------- |
|<img src="https://i.imgur.com/Zn3q0sg.gif" style="width:256px">|<img src="https://i.imgur.com/OrToXUA.gif" style="width:256px">|
|<img src="https://i.imgur.com/CsTay1G.jpg" style="width:256px">|<img src="https://i.imgur.com/CW37XiS.jpg" style="width:256px">|
|<img src="https://i.imgur.com/dKQ5OwC.jpg" style="width:256px">|<img src="https://i.imgur.com/NzfO7f8.jpg" style="width:256px">|
|<img src="https://i.imgur.com/uU1SlyG.jpg" style="width:256px">|<img src="https://i.imgur.com/FzA8Vtc.jpg" style="width:256px">|
|<img src="https://i.imgur.com/7BdTOlV.jpg" style="width:256px">|<img src="https://i.imgur.com/C1Nv8KV.jpg" style="width:256px">|
|<img src="https://i.imgur.com/NHDTA3J.jpg" style="width:256px">|<img src="https://i.imgur.com/2cJK7td.jpg" style="width:256px">|
|<img src="https://i.imgur.com/aowOlat.jpg" style="width:256px">|<img src="https://i.imgur.com/zr08Q6t.jpg" style="width:256px">|