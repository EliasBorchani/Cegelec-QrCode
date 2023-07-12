# Getting started

Install Android Studio : [here](https://developer.android.com/studio)

Make sure to install a proper android and java SDK : 
- Java 17
- Android SDK 33

# Build the project 

You can now compile directly in the Android Studio IDE or in command line : `./gradlew assembleDebug` and you should be able to find the app in `app/build/output/apk/...`

# Update the project

All the screens can be found in `app/src/main/java/com/eborchani/station/qrcode/features/generate/ui/screens` and their UI can directly be modified there

If you need to update some logic about the screens or how to react to some events, you may need to update the corresponding ViewModel located in `app/src/main/java/com/eborchani/station/qrcode/features/generate/ui/viewmodel`

If you want to add a screen and navigate to it, you will need to add its route in the `app/src/main/java/com/eborchani/station/qrcode/features/generate/ui/navigation/AppNavigation.kt`. And then you will be able to navigate into it.