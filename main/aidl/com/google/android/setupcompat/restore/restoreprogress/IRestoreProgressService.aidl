/*
 * Copyright (C) 2025 The Android Open Source Project
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

package com.google.android.setupcompat.restore.restoreprogress;

import com.google.android.setupcompat.restore.restoreprogress.IRestoreProgressCallback;

/**
 * AIDL interface for the Restore service to provide progress updates of the restore process.
 * Client applications within the Setup flow can use this service to register for and receive
 * these progress updates via {@link IRestoreProgressCallback}.
 */
interface IRestoreProgressService {
    /** The action to bind to the {@link IRestoreProgressService}. */
    const String INTENT_ACTION = "com.google.android.setupcompat.restore.restoreprogress.RESTORE_PROGRESS_SERVICE_ACTION";

    /** The current version of the APIs provided by this AIDL interface. */
    const int VERSION = 1;

    /**
     * Registers a callback for restore progress updates.
     *
     * @deprecated Use {@link #registerCallbackForProgressUpdatesWithVersion(IRestoreProgressCallback, int)} instead.
     * This method no longer registers for callbacks.
     */
    oneway void registerCallbackForProgressUpdates(IRestoreProgressCallback callback) = 1;

    /** Unregisters a callback for restore progress updates. */
    oneway void unregisterCallbackForProgressUpdates(IRestoreProgressCallback callback) = 2;

    /** Notifies the service that the progress indicator was clicked. */
    oneway void notifyProgressIndicatorClicked() = 3;

    /** Registers a callback for restore progress updates for the given version of the api. */
    oneway void registerCallbackForProgressUpdatesWithVersion(IRestoreProgressCallback callback, int apiVersion) = 4;

    oneway void notifyTooltipShown() = 5;

}
