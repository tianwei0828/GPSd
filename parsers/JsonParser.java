package com.tw.gpsd.parsers;

import com.google.gson.JsonObject;
import com.tw.gpsd.GPSdException;
import com.tw.gpsd.results.json.GPS;
import com.tw.gpsd.results.json.TPV;
import com.tw.gpsd.utils.GsonUtil;
import com.tw.socket.utils.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public final class JsonParser implements Parser<GPS> {
    private static final String TAG = "JsonParser";
    private final DateFormat dateFormat;

    public JsonParser() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public GPS parse(String data) throws GPSdException {
        Log.i(TAG, "beforeParse: " + data);
        GPS gps = null;
        try {
            JsonObject jsonObject = new com.google.gson.JsonParser().parse(data).getAsJsonObject();
            String clazz = jsonObject.get("class").getAsString();
            Log.i(TAG, "clazz: " + clazz);
            if (TPV.class.getSimpleName().equals(clazz)) {
                gps = GsonUtil.fromJson(data, TPV.class);
            }
            Log.i(TAG, "afterParse: " + gps);
        } catch (final Exception e) {
            throw new GPSdException("JsonParse failed", e);
        }
        return gps;
    }
}
