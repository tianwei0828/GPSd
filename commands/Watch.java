package com.tw.gpsd.commands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tw.gpsd.utils.GsonUtil;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public final class Watch implements Command {
    public static final String WATCH = "WATCH";
    public static final String KEY_ENABLE = "enable";
    public static final String KEY_JSON = "json";
    public static final String KEY_NMEA = "nmea";
    public static final String KEY_DEVICE = "device";

    @SerializedName("class")
    @Expose
    public String clazz = WATCH;
    @Expose
    public boolean enable;
    @Expose
    public boolean json;
    @Expose
    public boolean nmea;
    @Expose
    public String device;

    @Override
    public String toJson() {
        return GsonUtil.toCustomJson(this);
    }

    @Override
    public String toLine() {
        return "?" + WATCH + "=" + toJson() + "\n";
    }

    @Override
    public String toString() {
        return "Watch{" +
                "class='" + clazz + '\'' +
                ", enable='" + enable + '\'' +
                ", json='" + json + '\'' +
                ", nmea='" + nmea + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
