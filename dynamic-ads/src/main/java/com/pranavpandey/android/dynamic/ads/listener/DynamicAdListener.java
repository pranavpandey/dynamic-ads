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

package com.pranavpandey.android.dynamic.ads.listener;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

/**
 * An interface to listen the dynamic ad events.
 */
public interface DynamicAdListener {

    /**
     * Returns the context used by this listener.
     *
     * @return The context used by this listener.
     */
    @NonNull Context getAdContext();

    /**
     * This method will be called to detect if the ads are enabled for this listener.
     *
     * @return {@code true} if the ads are enabled for this listener.
     */
    boolean isAdEnabled();

    /**
     * This method will be called to get the ad event event count.
     *
     * @return The ad event count.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    long getAdEventCount();

    /**
     * This method will be called to increment the ad event count.
     *
     * @param eventCount The ad event count to be set.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    void setAdEventCount(long eventCount);

    /**
     * This method will be called to reset the ad event count.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    void resetAdEventCount();

    /**
     * This method will be called when an ad event has occurred.
     *
     * @see #setAdEventCount(long)
     */
    void onAdEvent();

    /**
     * This method will be called to get the container view to show the banner ad.
     *
     * @return The container view to show the banner ad.
     */
    @Nullable ViewGroup getAdContainer();

    /**
     * This method will be called when the ad view is loaded.
     *
     * @param adView The loaded ad view.
     */
    void onAdDisplay(@NonNull AdView adView);

    /**
     * This method will be called when the interstitial ad is ready to be shown.
     *
     * @param interstitialAd The loaded interstitial ad.
     */
    void onAdDisplay(@NonNull InterstitialAd interstitialAd);

    /**
     * This method will be called when the native ad is ready to be shown.
     *
     * @param nativeAd The loaded native ad.
     * @param parent The optional parent view.
     * @param adView The optional native ad view.
     */
    void onAdDisplay(@NonNull NativeAd nativeAd, @Nullable View parent,
            @Nullable NativeAdView adView);
}
