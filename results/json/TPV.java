package com.tw.gpsd.results.json;

import com.google.gson.annotations.SerializedName;
import com.tw.gpsd.utils.JsonResultUtil;

/**
 * Created by wei.tian
 * 2019/4/18
 */
public class TPV implements GPS {

    /**
     * class : TPV
     * device : /dev/pts/1
     * mode : 3
     * time : 2019-04-18T08:21:37.000Z
     * ept : 0.005
     * lat : 31.341626377
     * lon : 121.497553698
     * alt : 69.007
     * epv : 15.272
     * track : 0
     * speed : 0.012
     * climb : -0
     * epc : 30.54
     */

    @SerializedName("class")
    private String clazz;
    private String device;
    private int mode;
    private String time;
    private double ept;
    private double lat;
    private double lon;
    private double alt;
    private double epv;
    private int track;
    private double speed;
    private String climb;
    private double epc;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Seconds since the Unix epoch, UTC. May have a fractional part of up to .01sec precision.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return JsonResultUtil.parse(this.time);
    }

    public double getEpt() {
        return ept;
    }

    public void setEpt(double ept) {
        this.ept = ept;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getEpv() {
        return epv;
    }

    public void setEpv(double epv) {
        this.epv = epv;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getClimb() {
        return climb;
    }

    public void setClimb(String climb) {
        this.climb = climb;
    }

    public double getEpc() {
        return epc;
    }

    public void setEpc(double epc) {
        this.epc = epc;
    }

    @Override
    public String toString() {
        return "TPV{" +
                "clazz='" + clazz + '\'' +
                ", device='" + device + '\'' +
                ", mode=" + mode +
                ", time='" + time + '\'' +
                ", timestamp='" + getTimestamp() + '\'' +
                ", ept=" + ept +
                ", lat=" + lat +
                ", lon=" + lon +
                ", alt=" + alt +
                ", epv=" + epv +
                ", track=" + track +
                ", speed=" + speed +
                ", climb='" + climb + '\'' +
                ", epc=" + epc +
                '}';
    }
}
