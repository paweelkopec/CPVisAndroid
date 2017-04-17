package com.paweelk.cpvisandroid.model;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 17.04.17.
 */

public class Settings {

    private int intervalMillisecond;

    public Settings setIntervalMillisecond(int intervalMillisecond) {
        this.intervalMillisecond = intervalMillisecond;
        return this;
    }

    public int getIntervalMillisecond() {
        return intervalMillisecond;
    }
}
