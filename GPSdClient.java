package com.tw.gpsd;

import com.tw.gpsd.callback.JsonResultCallback;
import com.tw.gpsd.callback.NMEAResultCallback;
import com.tw.gpsd.callback.RawDataResultCallback;
import com.tw.gpsd.commands.Command;
import com.tw.gpsd.commands.Watch;
import com.tw.gpsd.parsers.JsonParser;
import com.tw.gpsd.parsers.NMEAParser;
import com.tw.gpsd.results.json.GPS;
import com.tw.gpsd.results.json.TPV;
import com.tw.socket.client.SocketClient;
import com.tw.socket.utils.Log;
import com.tw.socket.utils.ObjectUtil;
import com.tw.socket.utils.StringUtil;
import com.tw.socket.utils.TimeUtil;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A GPSd client with Java
 * 参考http://www.catb.org/gpsd/client-howto.html
 */
public final class GPSdClient {
    private static final String TAG = "GPSdClient";
    private final String server;
    private final int port;
    private boolean isDaemon = false;
    private int retryInterval = 2000;
    private int retryCount = 3;
    private int connectTimeout = 0;
    private int readTimeout = 1000 * 60 * 2;
    private boolean isDebug;
    private String logPath;
    private String logName;
    private boolean isCover;
    SocketClient socketClient;
    private JsonParser jsonParser;
    private NMEAParser nmeaParser;
    private final List<RawDataResultCallback> rawDataResultCallbacks;
    private final List<JsonResultCallback> jsonResultCallbacks;
    private final List<NMEAResultCallback> nmeaResultCallbacks;


    public GPSdClient(String server, int port) {
        this.server = StringUtil.requireNotBlank(server, "server is blank");
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("illegal port number: " + port);
        }
        this.port = port;
        this.rawDataResultCallbacks = new ArrayList<>();
        this.jsonResultCallbacks = new ArrayList<>();
        this.nmeaResultCallbacks = new ArrayList<>();
    }

    public void isDaemon(boolean isDaemon) {
        this.isDaemon = isDaemon;
    }

    public void retryInterval(int retryInterval) {
        this.retryInterval = TimeUtil.checkDuration("retryInterval", retryInterval, TimeUnit.MILLISECONDS);
    }

    public void retryCount(int retryCount) {
        this.retryCount = TimeUtil.checkDuration("retryCount", retryCount, TimeUnit.MILLISECONDS);
    }

    public void readTimeout(int readTimeout) {
        this.readTimeout = TimeUtil.checkDuration("readTimeout", readTimeout, TimeUnit.MILLISECONDS);
    }

    public void connectTimeout(int connectTimeout) {
        if (connectTimeout < 0) {
            throw new IllegalArgumentException("connectTimeout < 0");
        }
        this.connectTimeout = connectTimeout;
    }

    public void isDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void logPath(String logPath) {
        this.logPath = com.tw.socket.utils.StringUtil.requireNotBlank(logPath, "logPath == null");
    }

    public void logName(String logName) {
        this.logName = com.tw.socket.utils.StringUtil.requireNotBlank(logName, "logName == null");
    }

    public void isCover(boolean isCover) {
        this.isCover = isCover;
    }

    public void setJsonParser(JsonParser jsonParser) {
        this.jsonParser = ObjectUtil.requireNotNull(jsonParser, "jsonParser == null");
    }

    public void setNMEAParser(NMEAParser nmeaParser) {
        this.nmeaParser = ObjectUtil.requireNotNull(nmeaParser, "nmeaParser == null");
    }

    public void addRawDataResultCallback(RawDataResultCallback rawDataResultCallback) {
        ObjectUtil.checkNotNull(rawDataResultCallback, "rawDataResultCallback == null");
        synchronized (rawDataResultCallbacks) {
            rawDataResultCallbacks.add(rawDataResultCallback);
        }
    }

    public void removeRawDataResultCallback(RawDataResultCallback rawDataResultCallback) {
        ObjectUtil.checkNotNull(rawDataResultCallback, "rawDataResultCallback == null");
        synchronized (rawDataResultCallbacks) {
            rawDataResultCallbacks.remove(rawDataResultCallback);
        }
    }

    public void addJsonResultCallback(JsonResultCallback jsonResultCallback) {
        ObjectUtil.checkNotNull(jsonResultCallback, "jsonResultCallback == null");
        synchronized (jsonResultCallbacks) {
            jsonResultCallbacks.add(jsonResultCallback);
        }
    }

    public void removeJsonResultCallback(JsonResultCallback jsonResultCallback) {
        ObjectUtil.checkNotNull(jsonResultCallback, "jsonResultCallback == null");
        synchronized (jsonResultCallbacks) {
            jsonResultCallbacks.remove(jsonResultCallback);
        }
    }

    public void addNMEAResultCallback(NMEAResultCallback nmeaResultCallback) {
        ObjectUtil.checkNotNull(nmeaResultCallback, "nmeaResultCallback == null");
        synchronized (nmeaResultCallbacks) {
            nmeaResultCallbacks.add(nmeaResultCallback);
        }
    }

    public void removeNMEAResultCallback(NMEAResultCallback nmeaResultCallback) {
        ObjectUtil.checkNotNull(nmeaResultCallback, "nmeaResultCallback == null");
        synchronized (nmeaResultCallbacks) {
            nmeaResultCallbacks.remove(nmeaResultCallback);
        }
    }

    public void connect() {
        if (socketClient != null) {
            return;
        }
        socketClient = new SocketClient(this.server, this.port);
        socketClient.retryCount(retryCount);
        socketClient.retryInterval(retryInterval);
        socketClient.connectTimeout(connectTimeout);
        socketClient.readTimeout(readTimeout);
        socketClient.isDebug(isDebug);
        socketClient.logPath(logPath);
        socketClient.logName(logName);
        socketClient.isCover(isCover);
        socketClient.callback(new SocketClient.Callback<String>() {
            @Override
            public void onDataChanged(String data) {
                Log.i(TAG, "收到播发数据的时间: " + System.currentTimeMillis());
                Log.i(TAG, "onDataChanged: " + data);
                if (StringUtil.isBlank(data)) {
                    return;
                }
                broadcastDataChanged(data);
                Log.i(TAG, "转发完数据的时间: " + System.currentTimeMillis());
            }

            @Override
            public void onStatusChanged(int status, String message) {
                Log.i(TAG, "onStatusChanged: " + status + " " + message);
                broadcastStatusChanged(status, message);
            }
        });
        socketClient.connect();
    }

    public Request newRequest(Command command) {
        return new RealRequest(this, command);
    }

    public void close() {
        if (socketClient != null) {
            Watch unwatch = new Watch();
            unwatch.enable = false;
            newRequest(unwatch).send();
            socketClient.close();
            socketClient = null;
        }
    }

    public String server() {
        return server;
    }

    public int port() {
        return port;
    }

    public boolean isDaemon() {
        return isDaemon;
    }

    public int retryInterval() {
        return retryInterval;
    }

    public int retryCount() {
        return retryCount;
    }

    public String logPath() {
        return logPath;
    }

    public String logName() {
        return logName;
    }

    public boolean isCover() {
        return isCover;
    }

    public JsonParser jsonParser() {
        return jsonParser;
    }

    public NMEAParser nmeaParser() {
        return nmeaParser;
    }

    public List<RawDataResultCallback> rawDataResultCallbacks() {
        synchronized (rawDataResultCallbacks) {
            return rawDataResultCallbacks;
        }
    }

    public List<JsonResultCallback> jsonResultCallbacks() {
        synchronized (jsonResultCallbacks) {
            return jsonResultCallbacks;
        }
    }

    public List<NMEAResultCallback> nmeaResultCallbacks() {
        synchronized (nmeaResultCallbacks) {
            return nmeaResultCallbacks;
        }
    }

    private void broadcastRawDataChanged(String data) {
        synchronized (rawDataResultCallbacks) {
            if (rawDataResultCallbacks != null && rawDataResultCallbacks.size() > 0) {
                for (RawDataResultCallback callback : rawDataResultCallbacks) {
                    if (callback != null) {
                        callback.onRawData(data);
                    }
                }
            }
        }
    }

    private void broadcastJsonResultChanged(String data) {
        synchronized (jsonResultCallbacks) {
            if (jsonResultCallbacks != null && jsonResultCallbacks.size() > 0) {
                if (jsonParser != null) {
                    try {
                        GPS gps = jsonParser.parse(data);
                        if (gps instanceof TPV) {
                            for (JsonResultCallback callback : jsonResultCallbacks) {
                                if (callback != null) {
                                    callback.onTPV((TPV) gps);
                                }
                            }
                        }
                    } catch (GPSdException e) {
                        //ignore
                    }
                    //TODO:parse other json result
                }
            }
        }
    }

    private void broadcastNMEAResultChanged(String data) {
        synchronized (nmeaResultCallbacks) {
            if (nmeaResultCallbacks != null && nmeaResultCallbacks.size() > 0) {
                if (nmeaParser != null) {
                    try {
                        Sentence sentence = nmeaParser.parse(data);
                        if (sentence instanceof GGASentence) {
                            for (NMEAResultCallback callback : nmeaResultCallbacks) {
                                if (callback != null) {
                                    callback.onGGASentence((GGASentence) sentence);
                                }
                            }
                        } else if (sentence instanceof RMCSentence) {
                            for (NMEAResultCallback callback : nmeaResultCallbacks) {
                                if (callback != null) {
                                    callback.onRMCSentence((RMCSentence) sentence);
                                }
                            }
                        }
                    } catch (GPSdException e) {
                        //ignore
                    }
                    //TODO:parse other nmea sentence
                }
            }
        }
    }

    private void broadcastDataChanged(String data) {
        broadcastRawDataChanged(data);
        broadcastJsonResultChanged(data);
        broadcastNMEAResultChanged(data);
    }


    private void broadcastStatusChanged(int status, String message) {
        synchronized (rawDataResultCallbacks) {
            if (rawDataResultCallbacks != null && rawDataResultCallbacks.size() > 0) {
                for (RawDataResultCallback callback : rawDataResultCallbacks) {
                    if (callback != null) {
                        callback.onStatusChanged(status, message);
                    }
                }
            }
        }
        synchronized (jsonResultCallbacks) {
            if (jsonResultCallbacks != null && jsonResultCallbacks.size() > 0) {
                for (JsonResultCallback callback : jsonResultCallbacks) {
                    if (callback != null) {
                        callback.onStatusChanged(status, message);
                    }
                }
            }
        }
        synchronized (nmeaResultCallbacks) {
            if (nmeaResultCallbacks != null && nmeaResultCallbacks.size() > 0) {
                for (NMEAResultCallback callback : nmeaResultCallbacks) {
                    if (callback != null) {
                        callback.onStatusChanged(status, message);
                    }
                }
            }
        }
    }
}
