/*
 * Copyright 2022 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.ads;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

/**
 * An abstract class to implement a {@link DynamicAd}.
 */
public abstract class DynamicBaseAd implements DynamicAd {

    @SuppressLint("VisibleForTests")
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

    @Override
    public boolean isAdAllowed() {
        return true;
    }

    @Override
    public void onCustomiseAd(boolean loaded) { }

    @Override
    public void onPostAdLoaded(boolean loaded) {
        populateAd();
    }

    @Override
    public void populateAd() { }
}
