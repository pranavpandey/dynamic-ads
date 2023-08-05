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

package com.pranavpandey.android.dynamic.ads.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.ads.DynamicAds;
import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView;

/**
 * A {@link DynamicItemView} to provide the consent information.
 */
public class DynamicConsentView extends DynamicItemView {

    public DynamicConsentView(@NonNull Context context) {
        super(context);
    }

    public DynamicConsentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicConsentView(@NonNull Context context,
            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        if (getContext() instanceof BaseAdListener) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DynamicAds.getInstance().loadConsentInformation(
                            (BaseAdListener) getContext(), true);
                }
            });

            setVisibility(((BaseAdListener) getContext()).isAdEnabled()
                    && DynamicAds.getInstance().isConsentFormAvailable() ? VISIBLE : GONE);
        } else {
            setOnClickListener(null);
            setVisibility(GONE);
        }
    }
}
