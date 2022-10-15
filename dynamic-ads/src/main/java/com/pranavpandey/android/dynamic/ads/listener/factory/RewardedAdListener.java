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

import androidx.annotation.NonNull;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;

/**
 * A {@link BaseAdListener} to listen the rewarded ad events.
 */
public interface RewardedAdListener extends BaseAdListener {

    /**
     * This method will be called when the rewarded ad is ready to be shown.
     *
     * @param rewardedAd The loaded rewarded ad.
     */
    void onAdDisplay(@NonNull RewardedAd rewardedAd);
}
