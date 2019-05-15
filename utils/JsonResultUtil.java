package com.tw.gpsd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by wei.tian
 * 2019/4/18
 */
public final class JsonResultUtil {
    private JsonResultUtil() {
        throw new IllegalStateException("No instance");
    }

    private static final SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static Long parse(String time) {
        try {
            final Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            //ignore
        }
        return 0L;
    }
}
