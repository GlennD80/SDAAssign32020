package com.example.sdaassign32020;
/*
* Copyright (C) 2016 The Android Open Source Project
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


/**
 * {@link FlavorAdapter} represents a single Android platform release.
 * Each object has 3 properties: name, version number, and image resource ID.
 * This is a basic arrayAdapter
 */
public class FlavorAdapter {

    // Name of the tee shirt (e.g. Alien, Celtic )
    private String mShirtName;

    // tee shirt sizes (e.g. S, M, L, XL, XXL)
    private String mSizeNumber;

    // Drawable resource ID
    private int mImageResourceId;

    /*
     * Create a new FlavorAdapter object.
     * @param vName is the name of the Android version (e.g. Gingerbread)
     * @param vNumber is the corresponding Android version number (e.g. 2.3-2.7)
     * @param image is drawable reference ID that corresponds to the Android version
     *
     * */
    public FlavorAdapter(String vName, String vNumber, int imageResourceId)
    {
        mShirtName = vName;
        mSizeNumber = vNumber;
        mImageResourceId = imageResourceId;
    }

    /**
     * Get the name of the tee shirt name
     */
    public String getName() {
        return mShirtName;
    }

    /**
     * Get the tee shirt size
     */
    public String getNumber() {
        return mSizeNumber;
    }

    /**
     * Get the image resource ID
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

}

