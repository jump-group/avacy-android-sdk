# Avacy CMP

Avacy CMP is the Android library for the Smart Consent Solution provided by Avacy

## Requirements

* Android Studio
* minSdkVersion 19 or higher


## Import the SDK

Apps can import the SDK with a Gradle dependency that points to jitpack repository.  
First, make sure that jitpack is referenced in the allprojects section of your project-level build.gradle file.

```
allprojects {
    repositories {
		...
		maven { url "https://jitpack.io" }
		...
	}
}

```

Next, open the app-level build.gradle file for your app, and look for a "dependencies" section.

```
dependencies {
	...
    implementation 'com.github.jump-group:avacy-android-sdk:1.0.0'
	...
}
```
Add the line above, which instruct Gradle to pull in the latest version of the SDK.

If not already there, add also the Kotlin dependency.

```
dependencies {
    ...
	implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.4.21'
    ...
}
```

Once that's done, save the file and perform a Gradle sync.

## Update your AndroidManifest.xml

To perform network operations in your application, your manifest must include the following permissions:

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />	
```

## Initialize the SDK

Add the following to the onCreate method in your Application class.  
Note: BASE_URL is url of the consents page provided by Avacy.

```
AvacyCMP.init(
	this,
	BASE_URL
)
```
## Implement features

**Check for consent**

Add the following for check if consent has already been given to the latest version of the privacy policy, if not, show the consent banner.  
Note: context must necessarily be a UI context

```
AvacyCMP.check(context)
```

Or use the following for catch any errors during loading

```
AvacyCMP.check(context, object : AvacyCMP.OnCMPReady() {
	override fun onError(error: String?) {
		...
	}
})
```

**Show Preference Center**

Add the following for show the Preference Center to edit current consents.  
Note: context must necessarily be a UI context

```
AvacyCMP.showPreferenceCenter(context)
```

or

```
AvacyCMP.check(context, object : AvacyCMP.OnCMPReady() {
	override fun onError(error: String?) {
		...
	}
})
```