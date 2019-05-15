package com.tw.gpsd.parsers;

import com.tw.gpsd.GPSdException;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public interface Parser<T> {
    T parse(String data) throws GPSdException;
}
