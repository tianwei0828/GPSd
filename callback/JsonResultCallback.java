package com.tw.gpsd.callback;

import com.tw.gpsd.GPSdException;
import com.tw.gpsd.results.json.TPV;

/**
 * Created by wei.tian
 * 2019/4/18
 */
public interface JsonResultCallback extends Callback {
    /**
     * @param tpv the TPV object
     */
    void onTPV(TPV tpv);

//    /**
//     * @param sky the SKY object
//     */
//    void onSKY(SKY sky);
//
//    /**
//     * @param att the ATT object
//     */
//    void onATT(ATT att);
//
//    /**
//     * @param subframe the SUBFRAME object
//     */
//    void onSUBFRAME(SUBFRAME subframe);
//
//    /**
//     * @param devices the devices object
//     */
//    void onDevices(Devices devices);
//
//    /**
//     * @param device the device object
//     */
//    void onDevice(Device device);

    void onGPSdException(GPSdException e);
}
