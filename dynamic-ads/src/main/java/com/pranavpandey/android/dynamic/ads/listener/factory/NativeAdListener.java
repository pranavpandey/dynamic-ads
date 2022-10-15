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

package com.pranavpandey.android.dynamic.ads.listener.factory;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

/**
 * A {@link ContainerAdListener} to listen the native ad events.
 */
public interface NativeAdListener extends ContainerAdListener {

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
