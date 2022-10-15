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

package com.pranavpandey.android.dynamic.ads.factory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.listener.factory.InterstitialAdListener;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;

/**
 * A {@link DynamicBaseAd} to show an {@link InterstitialAd} dynamically throughout the app.
 */
public class DynamicInterstitialAd extends DynamicBaseAd
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Interstitial ad listener to listen ad events.
     */
    private final InterstitialAdListener mInterstitialAdListener;

    /**
     * Minimum event count to show this ad.
     */
    private final long mEventCount;

    /**
     * Interstitial ad loaded by this dynamic ad.
     */
    private InterstitialAd mInterstitialAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The interstitial ad listener to be used.
     */
    public DynamicInterstitialAd(@NonNull String adUnitId,
            @NonNull InterstitialAdListener dynamicAdListener) {
        this(adUnitId, dynamicAdListener, Default.EVENT_COUNT);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The interstitial ad listener to be used.
     * @param eventCount The event count to be used.
     */
    public DynamicInterstitialAd(@NonNull String adUnitId,
            @NonNull InterstitialAdListener dynamicAdListener, long eventCount) {
        this.mAdUnitId = adUnitId;
        this.mInterstitialAdListener = dynamicAdListener;
        this.mEventCount = eventCount;

        if (getAdListener().isAdEnabled()) {
            onInitialize();
        }
    }

    @Override
    public @NonNull String getAdUnitId() {
        return mAdUnitId;
    }

    @Override
    public @LayoutRes int getAdLayoutRes() {
        return LAYOUT_RES_NONE;
    }
    
    @Override
    public @NonNull InterstitialAdListener getAdListener() {
        return mInterstitialAdListener;
    }

    @Override
    public void onInitialize() {
        DynamicAds.initializeInstance(getAdListener().getAdContext());
        DynamicAds.getInstance().initializeAd(this, getAdListener());
    }

    @Override
    public @Nullable ConsentInformation getConsentInformation() {
        return DynamicAds.getInstance().getConsentInformation();
    }

    @Override
    public @Nullable ConsentForm getConsentForm() {
        return DynamicAds.getInstance().getConsentForm();
    }

    /**
     * Returns the optional full screen content callback.
     *
     * @return The optional full screen content callback.
     */
    protected @Nullable FullScreenContentCallback getFullScreenContentCallback() {
        return new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();

                onAdDestroy();
                onAdCreate();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);

                onAdDestroy();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();

                getAdListener().resetAdEventCount();
            }
        };
    }

    @Override
    public void onAdCreate() {
        if (!getAdListener().isAdEnabled()) {
            onAdDestroy();

            return;
        }

        if (DynamicAds.getInstance().isConsentRequired()) {
            return;
        }

        if (mInterstitialAd != null) {
            populateAd();

            return;
        }

        try {
            InterstitialAd.load(getAdListener().getAdContext(), getAdUnitId(),
                    getAdRequest(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);

                    mInterstitialAd = interstitialAd;

                    mInterstitialAd.setFullScreenContentCallback(getFullScreenContentCallback());

                    populateAd();
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * Try to call the ad display event for the loaded interstitial ad after comparing
     * the ad event count.
     *
     * @see InterstitialAdListener#getAdEventCount()
     * @see InterstitialAdListener#onAdDisplay(InterstitialAd)
     * @see InterstitialAd#show(Activity)
     */
    @Override
    public void populateAd() {
        super.populateAd();

        if (mInterstitialAd == null) {
            return;
        }

        if (getAdListener().getAdEventCount() >= mEventCount) {
            getAdListener().onAdDisplay(mInterstitialAd);
        }
    }

    @Override
    public void onAdResume() {
        getAdListener().getAdContext().getSharedPreferences(PREFS,
                Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAdPause() {
        getAdListener().getAdContext().getSharedPreferences(PREFS,
                Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAdDestroy() {
        mInterstitialAd = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (Key.EVENT_COUNT.equals(key)) {
            populateAd();
        }
    }
}
