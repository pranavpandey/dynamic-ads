<img src="./graphics/icon.png" width="160" height="160" align="right" hspace="20">

# Dynamic Ads

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Release](https://img.shields.io/maven-central/v/com.pranavpandey.android/dynamic-ads)](https://search.maven.org/artifact/com.pranavpandey.android/dynamic-ads)

A GDPR-compliant library to show ads via Google AdMob on Android 4.1 (API 16) and above.

> It uses [AndroidX][androidx] so, first [migrate][androidx-migrate] your project to AndroidX.
<br/>It is dependent on Java 8 due to the dependency on [Dynamic Utils][dynamic-utils].

---

## Contents

- [Installation](#installation)
- [Usage](#usage)
    - [Initialize](#initialize)
    - [Host](#host)
    - [Consent](#consent)
    - [Dynamic Ad](#dynamic-ad)
    - [Sponsor](#sponsor)
    - [Dependency](#dependency)
- [License](#license)

---

## Installation

It can be installed by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
  // For AndroidX enabled projects.
  implementation 'com.pranavpandey.android:dynamic-ads:1.3.0'
}
```

---

## Usage

It supports various ad formats as mentioned in the official Google [AdMob guide][admob guide].
Please [sign in][admob sign-in] to or [sign up][admob sign-up] for an AdMob account first.

> For a complete reference, please read the [documentation][documentation].

### Initialize

`DynamicAds` must be initialized once before accessing its methods. Dynamic ad formats do that
automatically before doing any operations but it should be done manually if anything has to be
done before.

```java
// Initialize with application context.
DynamicAds.initializeInstance(applicationContext);
```

### Host

`BaseAdListener` is the base class that an ad host like an `activity` must implement.
It guides the internal framework about how and when an ad should be displayed.

> It is used to create various factory [listeners][ad listeners] to show different ad formats
and they should be implemented according to the requirements.

### Consent

It provides built-in support for the consent form required to be shown in `GDPR` regions before
initializing any of the ads. It uses [User Messaging Platform (UMP)][admob ump] underneath to do
all the heavy lifting and provides a simple yet customizable interface.

```java
// Show a consent form.
DynamicAds.getInstance()
        // Set the consent request parameters required to load the consent information.
        .setConsentRequestParameters(consentRequestParameters)
        // Try to show the consent form if available
        .showConsentForm(dynamicAdListener);

// Other methods related to the consent form.
DynamicAds.getInstance()
        // Checks whether a consent information is available.
        .isConsentInformationAvailable()
        // Checks whether a consent form is available.
        .isConsentFormAvailable()
        // Checks whether a consent form is visible to the user.
        .isConsentFormVisible()
        // Checks whether a consent is required from the user.
        .isConsentRequired()
        // Returns the status of the consent information.
        .getConsetStatus();
```

### Dynamic Ad

`DynamicAd` is the base class to implement various [ad formats][ad formats].

```java
/**
 * An activity to host a dynamic ad.
 */
public AdHostActivity extends Activity implements BannerAdListener {
    
    ...
        
    // Initialize the banner ad.
    mDynamicBannerAd = new DynamicBannerAd(AD_UNIT_ID, this) {
        ...
    
        @Override
        public @NonNull AdRequest getAdRequest() {
            return new AdRequest.Builder().build();
        }
    
        @Override
        public @NonNull RequestConfiguration getAdRequestConfigurations() {
            return MobileAds.getRequestConfiguration().toBuilder()
                    .setTagForChildDirectedTreatment(
                    RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                    .setTagForUnderAgeOfConsent(
                    RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                    .build();
        }
          
        ...
    };

    @Override
    public @NonNull Context getAdContext() {
        // Return context for this host.
        return this;
    }

    @Override
    public boolean isAdEnabled() {
        // Ads are enabled for this host.
        return true;
    }
    
    ...

    @Override
    public void onAdDisplay(@NonNull AdView adView) {
        // Add the ad view to a container to make it visible.
        DynamicViewUtils.addView(container, adView, true);
    }
    
    ...

    @Override
    public void onResume() {
        super.onResume();

        // Resume a dynamic ad if supported.
        DynamicAds.onAdResume(mDynamicBannerAd);
    }

    @Override
    public void onPause() {
        // Pause a dynamic ad if supported.
        DynamicAds.onAdPause(mDynamicBannerAd);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy a dynamic ad if supported.
        DynamicAds.onAdDestroy(mDynamicBannerAd);
        super.onDestroy();
    }
}
```

### Sponsor

Please become a [sponsor][sponsor] to get a detailed guide and priority support.

### Dependency

It depends on the [dynamic-utils][dynamic-utils] to perform various internal operations. 
So, its functions can also be used to perform other useful operations.

---

## Author

Pranav Pandey

[![GitHub](https://img.shields.io/github/followers/pranavpandey?label=GitHub&style=social)](https://github.com/pranavpandey)
[![Follow on Twitter](https://img.shields.io/twitter/follow/pranavpandeydev?label=Follow&style=social)](https://twitter.com/intent/follow?screen_name=pranavpandeydev)
[![Donate via PayPal](https://img.shields.io/static/v1?label=Donate&message=PayPal&color=blue)](https://paypal.me/pranavpandeydev)

---

## License

    Copyright 2022-2023 Pranav Pandey

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[androidx]: https://developer.android.com/jetpack/androidx
[androidx-migrate]: https://developer.android.com/jetpack/androidx/migrate
[documentation]: https://pranavpandey.github.io/dynamic-ads
[sponsor]: https://github.com/sponsors/pranavpandey
[admob ump]: https://developers.google.com/admob/ump/android/quick-start
[admob sign-in]: https://admob.google.com/home
[admob sign-up]: https://support.google.com/admob/answer/7356219
[admob guide]: https://developers.google.com/admob/android/quick-start
[ad listeners]: https://github.com/pranavpandey/dynamic-ads/blob/main/dynamic-ads/src/main/java/com/pranavpandey/android/dynamic/ads/listener/factory
[ad formats]: https://github.com/pranavpandey/dynamic-ads/blob/main/dynamic-ads/src/main/java/com/pranavpandey/android/dynamic/ads/factory
[dynamic-utils]: https://github.com/pranavpandey/dynamic-utils
[dynamic-support]: https://github.com/pranavpandey/dynamic-support
