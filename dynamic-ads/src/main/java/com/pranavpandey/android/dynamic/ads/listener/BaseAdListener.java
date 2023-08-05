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

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * An interface to provide basic ads functionality.
 */
public interface BaseAdListener {

    /**
     * Returns the context used by this listener.
     *
     * @return The context used by this listener.
     */
    @NonNull Context getAdContext();

    /**
     * This method will be called to detect if the ads are enabled for this listener.
     *
     * @return {@code true} if the ads are enabled for this listener.
     */
    boolean isAdEnabled();
}
