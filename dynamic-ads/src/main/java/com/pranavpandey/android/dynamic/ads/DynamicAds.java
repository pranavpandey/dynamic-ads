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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.pranavpandey.android.dynamic.ads.listener.DynamicAdListener;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to handle the ads related operations including the consent information.
 * <p>It must be initialized once before accessing its methods.
 */
public class DynamicAds {

    /**
     * Singleton instance of {@link DynamicAds}.
     */
    @SuppressLint("StaticFieldLeak")
    private static DynamicAds sInstance;

    /**
     * Context to retrieve the resources.
     */
    private Context mContext;

    /**
     * Custom consent request parameters required to load the consent information.
     */
    private ConsentRequestParameters mConsentRequestParameters;

    /**
     * Consent information if available.
     */
    private ConsentInformation mConsentInformation;

    /**
     * Consent form if available.
     */
    private ConsentForm mConsentForm;

    /**
     * {@code true} if the consent for is visible.
     */
    private boolean mConsentFormVisible;

    /**
     * Backup ads to be reloaded.
     */
    private Set<DynamicAd> mBackupAds;

    /**
     * Main thread handler to publish results.
     */
    private final Handler mHandler;

    /**
     * {@code true} if the mobile ads have been initialized.
     */
    private boolean mInitialized;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #initializeInstance(Context)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private DynamicAds() {
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to be used.
     */
    private DynamicAds(@NonNull Context context) {
        this();

        this.mContext = context;
    }

    /**
     * Initialize ads when application starts.
     * <p>Must be initialized once.
     *
     * @param context The context to retrieve resources.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null.");
        }

        if (sInstance == null) {
            sInstance = new DynamicAds(!(context instanceof Application)
                    ? context.getApplicationContext() : context);
        }
    }

    /**
     * Retrieves the singleton instance of {@link DynamicAds}.
     * <p>Must be called before accessing the public methods.
     *
     * @return The singleton instance of {@link DynamicAds}.
     */
    public static synchronized @NonNull DynamicAds getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicAds.class.getSimpleName() +
                    " is not initialized, call initializeInstance(...) method first.");
        }

        return sInstance;
    }

    /**
     * Try to resume the dynamic ad.
     *
     * @param dynamicAd The dynamic ad to be used.
     *
     * @see DynamicAd#onAdResume()
     */
    public static void onAdResume(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd != null) {
            dynamicAd.onAdResume();
        }
    }

    /**
     * Try to pause the dynamic ad.
     *
     * @param dynamicAd The dynamic ad to be used.
     *
     * @see DynamicAd#onAdPause()
     */
    public static void onAdPause(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd != null) {
            dynamicAd.onAdPause();
        }
    }

    /**
     * Try to destroy the dynamic ad.
     *
     * @param dynamicAd The dynamic ad to be used.
     *
     * @see DynamicAd#onAdDestroy()
     */
    public static void onAdDestroy(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd != null) {
            dynamicAd.onAdDestroy();
        }
    }

    /**
     * Returns the dynamic ad event count from the shared preferences.
     *
     * @return The dynamic ad event count from the shared preferences.
     *
     * @see DynamicAd#PREFS
     * @see DynamicAd.Key#EVENT_COUNT
     */
    public static long getAdEventCount() {
        return DynamicPreferences.getInstance().load(DynamicAd.PREFS,
                DynamicAd.Key.EVENT_COUNT, DynamicAd.Value.EVENT_COUNT);
    }

    /**
     * Save the dynamic ad event count into the shared preferences.
     *
     * @param eventCount The ad event count to be set.
     *
     * @see DynamicAd#PREFS
     * @see DynamicAd.Key#EVENT_COUNT
     */
    public static void setAdEventCount(long eventCount) {
        DynamicPreferences.getInstance().save(DynamicAd.PREFS,
                DynamicAd.Key.EVENT_COUNT, eventCount);
    }

    /**
     * Reset the dynamic ad event count.
     *
     * @see DynamicAd#PREFS
     * @see DynamicAd.Key#EVENT_COUNT
     * @see DynamicAd.Value#EVENT_COUNT
     */
    public static void resetAdEventCount() {
        DynamicPreferences.getInstance().delete(DynamicAd.PREFS, DynamicAd.Key.EVENT_COUNT);
    }

    /**
     * Increment the saved ad event count by one.
     *
     * @see #setAdEventCount(long)
     */
    public static void onAdEvent() {
        setAdEventCount(getAdEventCount() + 1);
    }

    /**
     * Returns the context used by this instance.
     *
     * @return The context to retrieve the resources.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Returns the main thread handler to publish results.
     *
     * @return The main thread handler to publish results.
     */
    public @NonNull Handler getHandler() {
        return mHandler;
    }

    /**
     * Returns whether the mobile ads have been initialized.
     *
     * @return {@code true} if the mobile ads have been initialized.
     */
    public boolean isInitialized() {
        return mInitialized;
    }

    /**
     * Get the consent information if available.
     *
     * @return The consent information if available.
     */
    public @Nullable ConsentInformation getConsentInformation() {
        return mConsentInformation;
    }

    /**
     * Get the consent form if available.
     *
     * @return The consent form if available.
     */
    public @Nullable ConsentForm getConsentForm() {
        return mConsentForm;
    }

    /**
     * Checks whether a consent information is available.
     *
     * @return {@code true} if a consent information is available.
     */
    public boolean isConsentInformationAvailable() {
        return getConsentInformation() != null;
    }

    /**
     * Checks whether a consent form is available.
     *
     * @return {@code true} if a consent form is available.
     */
    public boolean isConsentFormAvailable() {
        return getConsentInformation() != null && getConsentInformation().isConsentFormAvailable();
    }

    /**
     * Checks whether a consent form is visible to the user.
     *
     * @return {@code true} if a consent form is visible to the user.
     */
    public boolean isConsentFormVisible() {
        return mConsentFormVisible;
    }

    /**
     * Returns the consent request parameters required to load the consent information.
     *
     * @return The consent request parameters required to load the consent information.
     */
    public @NonNull ConsentRequestParameters getConsentRequestParameters() {
        if (mConsentRequestParameters != null) {
            return mConsentRequestParameters;
        }

        return new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(true).build();
    }

    /**
     * Set the consent request parameters required to load the consent information.
     *
     * @param consentRequestParameters The parameters to be set.
     *                                 <p>Use {@code null} to set the default parameters.
     */
    public void setConsentRequestParameters(
            @Nullable ConsentRequestParameters consentRequestParameters) {
        this.mConsentRequestParameters = consentRequestParameters;
    }

    /**
     * Returns the status of the consent information.
     *
     * @return The status of the consent information.
     */
    public @ConsentInformation.ConsentStatus int getConsentStatus() {
        if (getConsentInformation() != null) {
            return getConsentInformation().getConsentStatus();
        }

        return ConsentInformation.ConsentStatus.UNKNOWN;
    }

    /**
     * Checks whether a consent is required from the user.
     *
     * @return {@code true} if a consent is required from the user.
     */
    public boolean isConsentRequired() {
        return getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED;
    }

    /**
     * Try to request the consent information update.
     *
     * @param dynamicAdListener The dynamic ad lister to be used.
     * @param force {@code true} to always show the consent form.
     */
    public void loadConsentInformation(final @Nullable DynamicAdListener dynamicAdListener,
            final boolean force) {
        this.mConsentInformation = UserMessagingPlatform.getConsentInformation(getContext());

        if (getConsentInformation() == null || !(dynamicAdListener instanceof Activity)) {
            return;
        }

        getConsentInformation().requestConsentInfoUpdate(
                (Activity) dynamicAdListener, getConsentRequestParameters(),
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {
                        if (isConsentFormAvailable()) {
                            loadConsentForm(dynamicAdListener, force);
                        } else {
                            recreateAds();
                        }
                    }},
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(@NonNull FormError formError) { }
                });
    }

    /**
     * Try to load the consent form.
     *
     * @param dynamicAdListener The dynamic ad lister to be used.
     * @param force {@code true} to always show the consent form.
     */
    public void loadConsentForm(final @Nullable DynamicAdListener dynamicAdListener,
            final boolean force) {
        if (dynamicAdListener == null) {
            return;
        }

        UserMessagingPlatform.loadConsentForm(getContext(),
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
            @Override
            public void onConsentFormLoadSuccess(@NonNull ConsentForm consentForm) {
                mConsentForm = consentForm;

                if (isConsentRequired() || force) {
                    showConsentForm(dynamicAdListener);
                } else {
                    recreateAds();
                }
            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
            @Override
            public void onConsentFormLoadFailure(@NonNull FormError formError) { }
        });
    }

    /**
     * Try to show the consent form.
     *
     * @param dynamicAdListener The dynamic ad lister to be used.
     */
    public void showConsentForm(final @Nullable DynamicAdListener dynamicAdListener) {
        if (getConsentForm() == null || !(dynamicAdListener instanceof Activity)) {
            return;
        }

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                getConsentForm().show((Activity) dynamicAdListener,
                        new ConsentForm.OnConsentFormDismissedListener() {
                    @Override
                    public void onConsentFormDismissed(@Nullable FormError formError) {
                        mConsentFormVisible = false;

                        loadConsentForm(dynamicAdListener, false);
                    }
                });

                mConsentFormVisible = true;
            }
        });
    }

    /**
     * Try to initialize the mobile ads.
     *
     * @param dynamicAd The dynamic ad to be initialized.
     * @param dynamicAdListener The dynamic ad lister to be used.
     */
    public void initializeAd(@Nullable DynamicAd dynamicAd,
            @Nullable DynamicAdListener dynamicAdListener) {
        if (!isConsentInformationAvailable()) {
            queueAd(dynamicAd);
            loadConsentInformation(dynamicAdListener, false);

            return;
        }

        if (isConsentFormVisible()) {
            showConsentForm(dynamicAdListener);
        }

        if (isConsentRequired()) {
            return;
        }

        postAd(dynamicAd);
    }

    /**
     * Try to create a dynamic ad on main thread.
     *
     * @param dynamicAd The dynamic ad to be created.
     *
     * @see DynamicAd#onAdCreate()
     */
    public void postAd(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd == null) {
            return;
        }

        try {
            if (!isInitialized()) {
                MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                        mInitialized = true;
                    }
                });
            }

            MobileAds.setRequestConfiguration(dynamicAd.getAdRequestConfigurations());
        } catch (Exception ignored) {
        }

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                createAd(dynamicAd);
            }
        });
    }

    /**
     * Try to create a dynamic ad.
     *
     * @param dynamicAd The dynamic ad to be created.
     *
     * @see DynamicAd#onAdCreate()
     */
    public void createAd(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd != null) {
            dynamicAd.onAdCreate();
        }
    }

    /**
     * Queue a dynamic ad to be reloaded later.
     *
     * @param dynamicAd The ad to be queued.
     */
    public void queueAd(@Nullable DynamicAd dynamicAd) {
        if (dynamicAd == null) {
            return;
        }

        if (mBackupAds == null) {
            mBackupAds = new HashSet<>();
        }

        mBackupAds.add(dynamicAd);
    }

    /**
     * Try to recreate all the queued ads.
     */
    public void recreateAds() {
        if (mBackupAds == null) {
            return;
        }

        for (DynamicAd dynamicAd : mBackupAds) {
            mBackupAds.remove(dynamicAd);
            postAd(dynamicAd);
        }
    }
}
