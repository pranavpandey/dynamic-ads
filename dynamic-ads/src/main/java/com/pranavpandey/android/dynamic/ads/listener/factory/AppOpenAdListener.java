/*
 * Copyright 2022-2024 Pranav Pandey
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

import androidx.annotation.NonNull;

import com.google.android.gms.ads.appopen.AppOpenAd;
import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;

/**
 * A {@link BaseAdListener} to listen the app open ad events.
 */
public interface AppOpenAdListener extends BaseAdListener {

    /**
     * This method will be called to get the desired ad orientation.
     *
     * @return The desired ad orientation.
     *
     * @deprecated The GMS Ads SDK now determines the orientation at request time,
     *             matching the behavior of other full-screen formats.
     */
    @Deprecated
    @AppOpenAd.AppOpenAdOrientation int getAdOrientation();

    /**
     * This method will be called when the app open ad is ready to be shown.
     *
     * @param appOpenAd The loaded app open ad.
     */
    void onAdDisplay(@NonNull AppOpenAd appOpenAd);
}
