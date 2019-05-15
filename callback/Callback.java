package com.tw.gpsd.callback;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public interface Callback {
    void onStatusChanged(int status, String message);
}
