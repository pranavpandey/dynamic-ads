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

package com.pranavpandey.android.dynamic.ads.listener;

import com.pranavpandey.android.dynamic.ads.listener.factory.AppOpenAdListener;
import com.pranavpandey.android.dynamic.ads.listener.factory.BannerAdListener;
import com.pranavpandey.android.dynamic.ads.listener.factory.InterstitialAdListener;
import com.pranavpandey.android.dynamic.ads.listener.factory.NativeAdListener;
import com.pranavpandey.android.dynamic.ads.listener.factory.RewardedAdListener;
import com.pranavpandey.android.dynamic.ads.listener.factory.RewardedInterstitialAdListener;

/**
 * An interface to listen the dynamic ad events.
 */
public interface DynamicAdListener extends BannerAdListener, InterstitialAdListener,
        NativeAdListener, RewardedAdListener, RewardedInterstitialAdListener, AppOpenAdListener { }
