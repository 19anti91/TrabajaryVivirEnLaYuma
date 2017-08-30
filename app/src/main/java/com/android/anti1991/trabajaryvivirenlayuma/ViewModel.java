package com.android.anti1991.trabajaryvivirenlayuma;

import android.support.annotation.NonNull;

/**
 * Created by Anti1991 on 8/28/2017.
 * Contains the data should be classifieds
 */

public class ViewModel {

    private String simpleText;

    /**
     * A viewmodel to hold and modify the data passed into it
     *
     * @param simpleText
     *         The initial text
     */
    public ViewModel(@NonNull final String simpleText) {
        setSimpleText(simpleText);
    }

    /**
     * Gets the text that has been set
     *
     * @return A String that represents the text that has been set
     */
    @NonNull
    public String getSimpleText() {
        return simpleText;
    }

    /**
     * While this is a basic project now, we could use this method to modify the
     * text after it was initially set
     *
     * @param simpleText
     *         The text that will be displayed in the itemview
     */
    public void setSimpleText(@NonNull final String simpleText) {
        this.simpleText = simpleText;
    }
}