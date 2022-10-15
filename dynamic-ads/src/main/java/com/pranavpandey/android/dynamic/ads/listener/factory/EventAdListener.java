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

import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;

/**
 * A {@link BaseAdListener} to listen the dynamic ad event count.
 */
public interface EventAdListener extends BaseAdListener {

    /**
     * This method will be called to get the ad event event count.
     *
     * @return The ad event count.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    long getAdEventCount();

    /**
     * This method will be called to increment the ad event count.
     *
     * @param eventCount The ad event count to be set.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    void setAdEventCount(long eventCount);

    /**
     * This method will be called to reset the ad event count.
     *
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd#PREFS
     * @see com.pranavpandey.android.dynamic.ads.DynamicAd.Key#EVENT_COUNT
     */
    void resetAdEventCount();

    /**
     * This method will be called when an ad event has occurred.
     *
     * @see #setAdEventCount(long)
     */
    void onAdEvent();
}
