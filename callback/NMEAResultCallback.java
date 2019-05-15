package com.tw.gpsd.callback;

import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;

/**
 * Created by wei.tian
 * 2019/4/18
 */
public interface NMEAResultCallback extends Callback {
    void onGGASentence(GGASentence ggaSentence);

    void onRMCSentence(RMCSentence rmcSentence);
}
