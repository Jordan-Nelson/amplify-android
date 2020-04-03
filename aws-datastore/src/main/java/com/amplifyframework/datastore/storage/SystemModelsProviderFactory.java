/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.datastore.storage;

import androidx.annotation.NonNull;

import com.amplifyframework.core.model.ModelProvider;
import com.amplifyframework.datastore.SimpleModelProvider;
import com.amplifyframework.datastore.appsync.ModelMetadata;
import com.amplifyframework.datastore.storage.sqlite.PersistentModelVersion;
import com.amplifyframework.datastore.syncengine.LastSyncMetadata;

/**
 * Creates a provide of system models, that are used internally by the DataStore.
 */
public final class SystemModelsProviderFactory {
    // CHANGE this models version whenever any system models are added/removed/updated.
    private static final String SYSTEM_MODELS_VERSION = "b9357be1-3106-475e-9700-7eaa6051a382";

    private SystemModelsProviderFactory() {}

    @NonNull
    public static ModelProvider create() {
        return SimpleModelProvider.instance(
            SYSTEM_MODELS_VERSION,

            // Metadata about the last time a model type was successfully sync'd with the cloud.
            // For example, "Post" model was last saved at 1585702708000 milliseconds past the Epoch.
            LastSyncMetadata.class,

            // PersistentModelVersion.class is stores the version of the data schema; that is,
            // which models exist in the system, and what is their shape. When the structure of
            // the data changes, this should see a version bump.
            PersistentModelVersion.class,

            // ModelMetadata.class stores the version of particular instances of a model. Unlike
            // PersistentModelVersion, which details with the structure of data, ModelMetadata
            // deals actually with individual records, and their states.
            ModelMetadata.class,

            // StorageItemChange.Record.class is an internal system event
            // it is used to stage local storage changes for upload to cloud
            StorageItemChange.Record.class
        );
    }
}