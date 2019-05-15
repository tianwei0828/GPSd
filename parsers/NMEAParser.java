package com.tw.gpsd.parsers;

import com.tw.gpsd.GPSdException;
import com.tw.socket.utils.Log;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.Sentence;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public final class NMEAParser implements Parser<Sentence> {
    private static final String TAG = "NMEAParser";

    @Override
    public Sentence parse(String data) throws GPSdException {
        Log.i(TAG, "beforeParse: " + data);
        Sentence sentence = null;
        try {
            sentence = SentenceFactory.getInstance().createParser(data);
        } catch (Exception e) {
            throw new GPSdException("NMEAParse failed", e);
        }
        Log.i(TAG, "afterParse: " + sentence);
        return sentence;
    }
}
