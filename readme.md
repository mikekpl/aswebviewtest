
# ☁️ ASWebView

Integrate AS' WebView anytime, anywhere on any Android application!

## Features
- AS based In App WebView
- Capability to Authenticate WebView
- A WebView that understands AS' ecosystem

## Get Started

In your project level ```build.gradle``` file make sure to add as plugin:
```
maven { url 'https://jitpack.io' }
```

and in your app level ```build.gradle```
```
implementation "com.github.mikekpl:aswebview:0.0.8"
```

## Usage

This library can be accessed and used by importing one important module in your Application level DI the ```asWebViewAppModule```

The rest is easy just by using Intents and opening up ```InAppWebViewActivity.kt```

**Example Usage:**
```
val intent = Intent(context, InAppWebViewActivity::class.java)
intent.putExtra("title", "Google")
intent.putExtra("url", "https://google.com")
context.startActivity(intent)
```

**List of Intent Extras and their purpose**

|Name|Usage|
|--|--|
|"title"|Sets the title of the Toolbar in the In App WebView|
|"url"|Sets the URL to be loaded in the In App WebView|
|"jwt"|JWT Token of Authenticated AS User|
|"isAuthenticated"|Makes the In App WebView Authenticated using AS SSO|
|"isExternal"|Opens up succeeding web links to external if set to true|