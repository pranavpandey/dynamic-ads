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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.listener.factory.BannerAdListener;
import com.pranavpandey.android.dynamic.ads.util.DynamicAdUtils;

/**
 * A {@link DynamicBaseAd} to show a banner ad dynamically throughout the app.
 */
public class DynamicBannerAd extends DynamicBaseAd {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Banner ad listener to listen ad events.
     */
    private final BannerAdListener mBannerAdListener;

    /**
     * Ad view used by the banner ad.
     */
    private AdView mAdView;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The banner ad listener to be used.
     */
    public DynamicBannerAd(@NonNull String adUnitId, @NonNull BannerAdListener dynamicAdListener) {
        this.mAdUnitId = adUnitId;
        this.mBannerAdListener = dynamicAdListener;

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
    public @NonNull BannerAdListener getAdListener() {
        return mBannerAdListener;
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

        try {
            mAdView = new AdView(getAdListener().getAdContext());
            mAdView.setAdUnitId(getAdUnitId());

            AdSize adSize = DynamicAdUtils.getAdSize(getAdListener().getAdContext());
            if (adSize != null) {
                mAdView.setAdSize(adSize);
            }

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    getAdListener().onAdDisplay(mAdView);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    mAdView.destroy();
                }
            });

            mAdView.loadAd(getAdRequest());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onAdResume() {
        if (mAdView == null) {
            return;
        }

        if (!getAdListener().isAdEnabled()) {
            onAdDestroy();
        } else {
            mAdView.resume();
        }
    }

    @Override
    public void onAdPause() {
        if (mAdView == null) {
            return;
        }

        mAdView.pause();
    }

    @Override
    public void onAdDestroy() {
        if (mAdView == null) {
            return;
        }

        mAdView.destroy();
        try {
            if (getAdListener().getAdContainer() != null
                    && getAdListener().getAdContainer().getChildCount() > 0) {
                getAdListener().getAdContainer().removeView(mAdView);
            }
        } catch (Exception ignored) {
        } finally {
            mAdView = null;
        }
    }
}
