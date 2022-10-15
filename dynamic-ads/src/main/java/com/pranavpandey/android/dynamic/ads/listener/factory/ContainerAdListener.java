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

import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;

/**
 * A {@link BaseAdListener} to provide ad container.
 */
public interface ContainerAdListener extends BaseAdListener {

    /**
     * This method will be called to get the container view to show the ad.
     *
     * @return The container view to show the ad.
     */
    @Nullable ViewGroup getAdContainer();
}