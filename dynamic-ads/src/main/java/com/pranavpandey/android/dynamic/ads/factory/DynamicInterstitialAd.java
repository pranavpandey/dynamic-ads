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
import com.pranavpandey.android.dynamic.ads.listener.DynamicAdListener;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;

/**
 * Helper class to show an interstitial ad dynamically throughout the app.
 */
public class DynamicInterstitialAd extends DynamicBaseAd
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Dynamic ad listener to listen ad events.
     */
    private final DynamicAdListener mDynamicAdListener;

    /**
     * Minimum event count to show this ad.
     */
    private final long mEventCount;

    /**
     * Interstitial ad loaded by this ad.
     */
    private InterstitialAd mInterstitialAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The dynamic ad listener to be used.
     */
    public DynamicInterstitialAd(@NonNull String adUnitId,
            @NonNull DynamicAdListener dynamicAdListener) {
        this(adUnitId, dynamicAdListener, Default.EVENT_COUNT);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The dynamic ad listener to be used.
     * @param eventCount The event count to be used.
     */
    public DynamicInterstitialAd(@NonNull String adUnitId,
            @NonNull DynamicAdListener dynamicAdListener, long eventCount) {
        this.mAdUnitId = adUnitId;
        this.mDynamicAdListener = dynamicAdListener;
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
    public @NonNull DynamicAdListener getAdListener() {
        return mDynamicAdListener;
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
            InterstitialAd.load(getAdListener().getAdContext(),
                    getAdUnitId(), getAdRequest(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);

                    mInterstitialAd = interstitialAd;

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();

                            onAdDestroy();
                            getAdListener().resetAdEventCount();
                            onAdCreate();
                        }
                    });

                    populateAd();
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * Try to show the loaded interstitial ad after comparing the ad event count.
     *
     * @see DynamicAdListener#getAdEventCount()
     * @see DynamicAdListener#onAdDisplay(InterstitialAd)
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
