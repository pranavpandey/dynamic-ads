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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.DynamicBaseAd;
import com.pranavpandey.android.dynamic.ads.R;
import com.pranavpandey.android.dynamic.ads.listener.factory.NativeAdListener;
import com.pranavpandey.android.dynamic.ads.util.DynamicAdUtils;

/**
 * Helper class to show a native ad dynamically throughout the app.
 */
public class DynamicNativeAd extends DynamicBaseAd {

    /**
     * Ad unit id used by this ad.
     */
    private final String mAdUnitId;

    /**
     * Optional layout resource used by this ad.
     */
    private final @LayoutRes int mAdLayoutRes;

    /**
     * Native ad listener to listen ad events.
     */
    private final NativeAdListener mDynamicAdListener;

    /**
     * Parent view used by the native ad.
     */
    private View mParentView;

    /**
     * Ad view used by the native ad.
     */
    private NativeAdView mAdView;

    /**
     * Native ad loaded by this ad.
     */
    private NativeAd mNativeAd;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param dynamicAdListener The native ad listener to be used.
     */
    public DynamicNativeAd(@NonNull String adUnitId,
            @NonNull NativeAdListener dynamicAdListener) {
        this(adUnitId, LAYOUT_RES_NONE, dynamicAdListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adUnitId The ad unit id to be used.
     * @param adLayoutRes The optional ad layout resource to be used.
     * @param dynamicAdListener The native ad listener to be used.
     */
    public DynamicNativeAd(@NonNull String adUnitId, @LayoutRes int adLayoutRes,
            @NonNull NativeAdListener dynamicAdListener) {
        this.mAdUnitId = adUnitId;
        this.mAdLayoutRes = adLayoutRes;
        this.mDynamicAdListener = dynamicAdListener;

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
        if (mAdLayoutRes == LAYOUT_RES_NONE) {
            try {
                Class.forName(CLASS_DYNAMIC_WIDGET);

                return R.layout.ada_native;
            } catch(Exception ignored) {
            }
        }

        return mAdLayoutRes;
    }

    @Override
    public @NonNull NativeAdListener getAdListener() {
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

        try {
            if (mNativeAd != null) {
                onCustomiseAd();
                
                return;
            }

            new AdLoader.Builder(getAdListener().getAdContext(), getAdUnitId())
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            mNativeAd = nativeAd;

                            onCustomiseAd();
                        }
                    }).build().loadAd(getAdRequest());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onCustomiseAd() {
        super.onCustomiseAd();

        if (mNativeAd == null || getAdLayoutRes() == LAYOUT_RES_NONE) {
            return;
        }

        mParentView = LayoutInflater.from(getAdListener().getAdContext())
                .inflate(getAdLayoutRes(), null);
        if ((mAdView = mParentView.findViewById(R.id.ada_native_ad)) == null) {
            return;
        }

        TextView primaryView = (TextView) mParentView.findViewById(R.id.ada_native_primary);
        TextView secondaryView = (TextView) mParentView.findViewById(R.id.ada_native_secondary);
        TextView bodyView = (TextView) mParentView.findViewById(R.id.ada_native_body);

        RatingBar ratingBar = (RatingBar) mParentView.findViewById(R.id.ada_native_rating_bar);
        DynamicAdUtils.setEnabled(ratingBar, false);

        Button callToActionView = (Button) mParentView.findViewById(R.id.ada_native_cta);
        ImageView iconView = (ImageView) mParentView.findViewById(R.id.ada_native_icon);
        MediaView mediaView = (MediaView) mParentView.findViewById(R.id.ada_native_media);

        String store = mNativeAd.getStore();
        String advertiser = mNativeAd.getAdvertiser();
        String headline = mNativeAd.getHeadline();
        String body = mNativeAd.getBody();
        String cta = mNativeAd.getCallToAction();
        Double starRating = mNativeAd.getStarRating();
        NativeAd.Image icon = mNativeAd.getIcon();

        mAdView.setCallToActionView(callToActionView);
        mAdView.setHeadlineView(primaryView);
        mAdView.setMediaView(mediaView);
        mAdView.setBodyView(bodyView);

        DynamicAdUtils.set(bodyView, body);
        DynamicAdUtils.setVisibility(secondaryView, View.VISIBLE);

        String secondaryText;
        if (DynamicAdUtils.isOnlyStore(mNativeAd)) {
            secondaryText = store;
            mAdView.setStoreView(secondaryView);
        } else if (!TextUtils.isEmpty(advertiser)) {
            secondaryText = advertiser;
            mAdView.setAdvertiserView(secondaryView);
        } else {
            secondaryText = null;
        }

        DynamicAdUtils.set(primaryView, headline);
        DynamicAdUtils.set(callToActionView, cta);

        if (starRating != null && starRating > 0) {
            DynamicAdUtils.setVisibility(secondaryView, View.GONE);
            DynamicAdUtils.setVisibility(ratingBar, View.VISIBLE);
            DynamicAdUtils.set(ratingBar, starRating.floatValue());
            mAdView.setStarRatingView(ratingBar);
        } else {
            DynamicAdUtils.set(secondaryView, secondaryText);
            DynamicAdUtils.setVisibility(ratingBar, View.GONE);
        }

        if (icon != null) {
            DynamicAdUtils.set(iconView, icon.getDrawable());
        } else {
            DynamicAdUtils.setVisibility(iconView, View.GONE);
        }

        populateAd();
    }

    @Override
    public void populateAd() {
        super.populateAd();

        if (mNativeAd == null || mAdView == null) {
            return;
        }


        mAdView.setNativeAd(mNativeAd);
        getAdListener().onAdDisplay(mNativeAd, mParentView, mAdView);
    }

    @Override
    public void onAdResume() {
        if (mAdView == null) {
            return;
        }

        if (!getAdListener().isAdEnabled()) {
            onAdDestroy();
        } else {
            onAdCreate();
        }
    }

    @Override
    public void onAdPause() { }

    @Override
    public void onAdDestroy() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }

        if (mAdView != null) {
            mAdView.destroy();
        } else if (mParentView == null) {
            return;
        }

        try {
            if (getAdListener().getAdContainer() != null
                    && getAdListener().getAdContainer().getChildCount() > 0) {
                getAdListener().getAdContainer().removeView(
                        mParentView != null ? mParentView : mAdView);
            }
        } catch (Exception ignored) {
        } finally {
            mAdView = null;
        }
    }
}
