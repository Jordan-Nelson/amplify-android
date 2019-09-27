/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package com.amplifyframework.hub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amplifyframework.core.async.Callback;
import com.amplifyframework.core.category.CategoryType;
import com.amplifyframework.core.plugin.Plugin;
import com.amplifyframework.core.plugin.PluginException;
import com.amplifyframework.core.task.Result;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultHubPlugin implements HubPlugin {

    private static final String TAG = DefaultHubPlugin.class.getSimpleName();

    private Context context;

    private static Map<Integer, HubCallback> listeners =
            new ConcurrentHashMap<Integer, HubCallback>();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public DefaultHubPlugin(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public void listen(HubChannel hubChannel, HubCallback callback) {
        listeners.put(hubChannel.hashCode(), callback);
    }

    @Override
    public void remove(HubChannel hubChannel, HubCallback callback) {
        listeners.remove(hubChannel.hashCode());
    }

    @Override
    public void dispatch(final HubChannel hubChannel, final HubPayload hubPayload) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    listeners.get(hubChannel.hashCode()).onHubEvent(hubPayload);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * @return the identifier that identifies
     * the plugin implementation
     */
    @Override
    public String getPluginKey() {
        return TAG;
    }

    /**
     * Configure the Plugin with the configuration passed.
     *
     * @param pluginConfiguration configuration for the plugin
     * @throws PluginException when configuration for a plugin was not found
     */
    @Override
    public void configure(@NonNull HubPluginConfiguration pluginConfiguration) throws PluginException {

    }

    /**
     * Configure the Plugin using the details in amplifyconfiguration.json
     *
     * @param context Android context required to read the contents of file
     * @throws PluginException when configuration for a plugin was not found
     */
    @Override
    public void configure(@NonNull Context context) throws PluginException {

    }

    /**
     * Reset the plugin to the state where it's not configured.
     */
    @Override
    public void reset() {

    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.HUB;
    }
}