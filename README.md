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
  - [Sponsor](#sponsor)
  - [Dependency](#dependency)
- [License](#license)

---

## Installation

It can be installed by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
  // For AndroidX enabled projects.
  implementation 'com.pranavpandey.android:dynamic-ads:1.1.0'
}
```

---

## Usage

It supports various ad formats as mentioned in the official Google [AdMob guide][admob guide].
Please [sign in][admob sign-in] to or [sign up][admob sign-up] for an AdMob account first.

> For a complete reference, please read the [documentation][documentation].

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

    Copyright 2022 Pranav Pandey

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
[admob sign-in]: https://admob.google.com/home
[admob sign-up]: https://support.google.com/admob/answer/7356219
[admob guide]: https://developers.google.com/admob/android/quick-start
[dynamic-utils]: https://github.com/pranavpandey/dynamic-utils
[dynamic-support]: https://github.com/pranavpandey/dynamic-support
