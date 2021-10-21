adar

Note: I've checked and fixed issues from the Android Studio "Inspect Code", the Lint kept giving me this error and 
I couldn't find the root of the issue in a timely matter:
```
izadi.egizabal adar % ./gradlew lint
> Task :app:kaptDebugKotlin FAILED

FAILURE: Build failed with an exception.

* What went wrong:
  Execution failed for task ':app:kaptDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask$KaptExecutionWorkAction
> java.lang.reflect.InvocationTargetException (no error message)
```
