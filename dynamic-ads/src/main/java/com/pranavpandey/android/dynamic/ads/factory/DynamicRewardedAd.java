/*
 * Copyright 2022-2023 Pranav Pandey
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
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.listener.factory.RewardedAdListener;

/**
 * A {@link DynamicBaseAd} to show a {@link RewardedAd} dynamically throughout the app.
 */
public class DynamicRewardedAd extends DynamicBaseAd {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Rewarded ad listener to listen ad events.
     */
    private final RewardedAdListener mRewardedAdListener;

    /**
     * Rewarded ad loaded by this dynamic ad.
     */
    private RewardedAd mRewardedAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The rewarded ad listener to be used.
     */
    public DynamicRewardedAd(@NonNull String adUnitId,
            @NonNull RewardedAdListener dynamicAdListener) {
        this.mAdUnitId = adUnitId;
        this.mRewardedAdListener = dynamicAdListener;

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
    public @NonNull RewardedAdListener getAdListener() {
        return mRewardedAdListener;
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

                setAdVisible(true);
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
            RewardedAd.load(getAdListener().getAdContext(), getAdUnitId(),
                    getAdRequest(), new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    super.onAdLoaded(rewardedAd);

                    mRewardedAd = rewardedAd;

                    if (getServerSideVerificationOptions() != null) {
                        mRewardedAd.setServerSideVerificationOptions(
                                getServerSideVerificationOptions());
                    }
                    mRewardedAd.setFullScreenContentCallback(getFullScreenContentCallback());

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
        return mRewardedAd != null;
    }

    @Override
    public void populateAd() {
        super.populateAd();

        if (!isAdLoaded()) {
            return;
        }

        getAdListener().onAdDisplay(mRewardedAd);
    }

    @Override
    public void onAdResume() { }

    @Override
    public void onAdPause() { }

    @Override
    public void onAdDestroy() {
        super.onAdDestroy();

        mRewardedAd = null;
    }
}
