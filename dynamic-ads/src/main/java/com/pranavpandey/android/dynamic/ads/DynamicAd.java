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

package com.pranavpandey.android.dynamic.ads;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.pranavpandey.android.dynamic.ads.listener.BaseAdListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An interface to implement a dynamic ad.
 */
public interface DynamicAd {

    /**
     * Constant for the no layout resource.
     */
    int LAYOUT_RES_NONE = -1;

    String CLASS_DYNAMIC_WIDGET =
            "com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget";

    /**
     * File name for the dynamic ads preferences.
     */
    String PREFS = "dynamic_ads";

    /**
     * An interface to hold key constants.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Key {

        /**
         * Key constant for the event_count.
         */
        String EVENT_COUNT = "ada_key_event_count";
    }

    /**
     * An interface to hold value constants.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Value {

        /**
         * Default value for the event count.
         */
        long EVENT_COUNT = 0;
    }

    /**
     * An interface to hold default values.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Default {

        /**
         * Default value for the event count.
         */
        long EVENT_COUNT = 8;
    }

    /**
     * Shared preferences key constant if GDPR applies.
     */
    String IABTCF_gdprApplies = "IABTCF_gdprApplies";

    /**
     * Shared preferences key constant for purpose consents.
     */
    String IABTCF_PurposeConsents = "IABTCF_PurposeConsents";

    /**
     * Shared preferences key constant for purpose legitimate interests.
     */
    String IABTCF_PurposeLegitimateInterests = "IABTCF_PurposeLegitimateInterests";

    /**
     * Shared preferences key constant for vendor consents.
     */
    String IABTCF_VendorConsents = "IABTCF_VendorConsents";

    /**
     * Shared preferences key constant for vendor legitimate interests.
     */
    String IABTCF_VendorLegitimateInterests = "IABTCF_VendorLegitimateInterests";

    /**
     * Returns the unit id for this ad.
     *
     * @return The unit id for this ad.
     */
    @NonNull String getAdUnitId();

    /**
     * Returns the optional layout resource for this ad.
     *
     * @return The optional layout resource for this ad.
     */
    @LayoutRes int getAdLayoutRes();

    /**
     * Returns the listener used by this ad.
     *
     * @return The listener used by this ad.
     */
    @NonNull BaseAdListener getAdListener();

    /**
     * This method will be called to do the initialization.
     */
    void onInitialize();

    /**
     * Returns the consent information if available.
     *
     * @return The consent information if available.
     */
    @Nullable ConsentInformation getConsentInformation();

    /**
     * Returns the consent form if available.
     *
     * @return The consent form if available.
     */
    @Nullable ConsentForm getConsentForm();

    /**
     * This method will be called to build the ad request for this ad.
     *
     * @return The ad request for this ad.
     */
    @NonNull AdRequest getAdRequest();

    /**
     * This method will be called to get the ad request configurations.
     *
     * @return The ad request configurations.
     */
    @NonNull RequestConfiguration getAdRequestConfigurations();

    /**
     * Try to create the new ad.
     */
    void onAdCreate();

    /**
     * This method will be called to do the ad customisations.
     */
    void onCustomiseAd();

    /**
     * This method will be called after the ad has been loaded.
     */
    void onPostAdLoaded();

    /**
     * Checks whether the ad has been loaded.
     *
     * @return {@code true} if the ad has been loaded.
     */
    boolean isAdLoaded();

    /**
     * Try to populate the new ad.
     */
    void populateAd();

    /**
     * Try to resume the paused ad.
     */
    void onAdResume();

    /**
     * Try to pause the running ad.
     */
    void onAdPause();

    /**
     * Try to destroy the ad instances and do cleanup.
     */
    void onAdDestroy();
}
