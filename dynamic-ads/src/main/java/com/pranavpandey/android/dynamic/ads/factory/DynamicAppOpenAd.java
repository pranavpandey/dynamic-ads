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

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.listener.factory.AppOpenAdListener;

/**
 * A {@link DynamicBaseAd} to show a {@link AppOpenAd} dynamically throughout the app.
 */
public class DynamicAppOpenAd extends DynamicBaseAd {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * App open ad listener to listen ad events.
     */
    private final AppOpenAdListener mAppOpenAdListener;

    /**
     * App open ad loaded by this dynamic ad.
     */
    private AppOpenAd mAppOpenAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The app open ad listener to be used.
     */
    public DynamicAppOpenAd(@NonNull String adUnitId,
            @NonNull AppOpenAdListener dynamicAdListener) {
        this.mAdUnitId = adUnitId;
        this.mAppOpenAdListener = dynamicAdListener;

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
    public @NonNull AppOpenAdListener getAdListener() {
        return mAppOpenAdListener;
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

        if (mAppOpenAd != null) {
            populateAd();

            return;
        }

        try {
            AppOpenAd.load(getAdListener().getAdContext(), getAdUnitId(), getAdRequest(),
                    getAdListener().getAdOrientation(), new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);

                    mAppOpenAd = appOpenAd;

                    mAppOpenAd.setFullScreenContentCallback(getFullScreenContentCallback());

                    populateAd();
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void populateAd() {
        super.populateAd();

        if (mAppOpenAd == null) {
            return;
        }

        getAdListener().onAdDisplay(mAppOpenAd);
    }

    @Override
    public void onAdResume() { }

    @Override
    public void onAdPause() { }

    @Override
    public void onAdDestroy() {
        mAppOpenAd = null;
    }
}
