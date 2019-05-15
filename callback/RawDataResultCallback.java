package com.tw.gpsd.callback;

/**
 * Created by wei.tian
 * 2019/4/18
 */
public interface RawDataResultCallback extends Callback {
    void onRawData(String rawData);
}
