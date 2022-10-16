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
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.listener.factory.RewardedInterstitialAdListener;

/**
 * A {@link DynamicBaseAd} to show a {@link RewardedInterstitialAd} dynamically throughout
 * the app.
 */
public class DynamicRewardedInterstitialAd extends DynamicBaseAd {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Rewarded interstitial ad listener to listen ad events.
     */
    private final RewardedInterstitialAdListener mRewardedInterstitialAdListener;

    /**
     * Rewarded interstitial ad loaded by this dynamic ad.
     */
    private RewardedInterstitialAd mRewardedInterstitialAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The rewarded interstitial ad listener to be used.
     */
    public DynamicRewardedInterstitialAd(@NonNull String adUnitId,
            @NonNull RewardedInterstitialAdListener dynamicAdListener) {
        this.mAdUnitId = adUnitId;
        this.mRewardedInterstitialAdListener = dynamicAdListener;

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
    public @NonNull RewardedInterstitialAdListener getAdListener() {
        return mRewardedInterstitialAdListener;
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
     * Returns the optional server side verification options.
     *
     * @return The optional server side verification options.
     */
    protected @Nullable ServerSideVerificationOptions getServerSideVerificationOptions() {
        return null;
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

        if (isAdLoaded()) {
            onCustomiseAd(true);
            onPostAdLoaded(true);

            return;
        }

        try {
            RewardedInterstitialAd.load(getAdListener().getAdContext(), getAdUnitId(),
                    getAdRequest(), new RewardedInterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                    super.onAdLoaded(rewardedInterstitialAd);

                    mRewardedInterstitialAd = rewardedInterstitialAd;

                    if (getServerSideVerificationOptions() != null) {
                        mRewardedInterstitialAd.setServerSideVerificationOptions(
                                getServerSideVerificationOptions());
                    }
                    mRewardedInterstitialAd.setFullScreenContentCallback(
                            getFullScreenContentCallback());

                    onCustomiseAd(false);
                    onPostAdLoaded(false);
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onPostAdLoaded(boolean loaded) { }

    @Override
    public boolean isAdLoaded() {
        return mRewardedInterstitialAd != null;
    }

    @Override
    public void populateAd() {
        super.populateAd();

        if (!isAdLoaded()) {
            return;
        }

        getAdListener().onAdDisplay(mRewardedInterstitialAd);
    }

    @Override
    public void onAdResume() { }

    @Override
    public void onAdPause() { }

    @Override
    public void onAdDestroy() {
        mRewardedInterstitialAd = null;
    }
}
