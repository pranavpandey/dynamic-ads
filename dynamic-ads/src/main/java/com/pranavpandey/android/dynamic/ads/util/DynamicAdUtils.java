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

package com.pranavpandey.android.dynamic.ads.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.pranavpandey.android.dynamic.util.DynamicWindowUtils;

/**
 * Helper class to perform ads related operations.
 */
public class DynamicAdUtils {

    /**
     * Factor to calculate the acceptable ad size.
     */
    public static final float AD_SIZE_FACTOR = 4f;

    /**
     * Returns the ad size for the supplied activity and container.
     *
     * @param activity The activity to calculate the ad size.
     * @param container The ad container to be used.
     *
     * @return The ad size for the supplied activity and container.
     *
     * @see AdSize#FLUID
     */
    @SuppressLint("VisibleForTests")
    public static @NonNull AdSize getAdSize(@Nullable Context activity,
            @Nullable View container) {
        if (!(activity instanceof Activity)) {
            return AdSize.FLUID;
        }

        float density = DynamicWindowUtils.getDisplayDensity(activity);
        Point screenSize = DynamicWindowUtils.getAppUsableScreenSize(activity);

        float adWidth = 0f;
        if (container != null && (adWidth = container.getWidth()) <= 0f) {
            adWidth = screenSize.x;
        }

        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                activity, (int) (adWidth / density));

        return adSize != AdSize.INVALID && adSize.getHeightInPixels(activity)
                < (screenSize.y / (density * AD_SIZE_FACTOR)) ? adSize : AdSize.FLUID;
    }

    /**
     * Returns the ad size for the supplied activity.
     *
     * @param activity The activity to calculate the ad size.
     *
     * @return The ad size for the supplied activity.
     *
     * @see #getAdSize(Context, View)
     */
    public static @NonNull AdSize getAdSize(@Nullable Context activity) {
        return getAdSize(activity, null);
    }

    /**
     * Returns the orientation for the app open ad.
     *
     * @param activity The activity to retrieve the screen orientation.
     *
     * @return The orientation for the app open ad.
     *
     * @see AppOpenAd#APP_OPEN_AD_ORIENTATION_PORTRAIT
     * @see AppOpenAd#APP_OPEN_AD_ORIENTATION_LANDSCAPE
     */
    public static @AppOpenAd.AppOpenAdOrientation int getAppOpenAdOrientation(
            @Nullable Context activity) {
        switch (DynamicWindowUtils.getScreenOrientation(activity)) {
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                return AppOpenAd.APP_OPEN_AD_ORIENTATION_LANDSCAPE;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
            default:
                return AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT;
        }
    }

    /**
     * Checks whether the ad has only the store data.
     *
     * @param nativeAd The ad to be checked.
     *
     * @return {@code true} if the ad has only the store data.
     */
    public static boolean isOnlyStore(@Nullable NativeAd nativeAd) {
        if (nativeAd == null) {
            return false;
        }

        return !TextUtils.isEmpty(nativeAd.getStore())
                && TextUtils.isEmpty(nativeAd.getAdvertiser());
    }

    /**
     * Set drawable for the image view and manage its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable to be set.
     */
    public static void set(@Nullable ImageView imageView, @Nullable Drawable drawable) {
        if (imageView == null) {
            return;
        }

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * Set text for the text view and manage its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void set(@Nullable TextView textView, @Nullable CharSequence text) {
        if (textView == null) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set rating value for the rating bar.
     *
     * @param ratingBar The rating bar to set the rating.
     * @param rating The rating value to be set.
     */
    public static void set(@Nullable RatingBar ratingBar, float rating) {
        if (ratingBar != null) {
            ratingBar.setRating(rating);
        }
    }

    /**
     * Set visibility for the view.
     *
     * @param view The view to set the visibility.
     * @param visibility The visibility to be set.
     */
    public static void setVisibility(@Nullable View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * Set a view enabled or disabled.
     *
     * @param view The view to be enabled or disabled.
     * @param enabled {@code true} to enable the view.
     */
    public static void setEnabled(@Nullable View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }
}
