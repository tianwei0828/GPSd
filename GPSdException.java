package com.tw.gpsd;

/**
 * Created by wei.tian
 * 2019/4/2
 */
public class GPSdException extends Exception {
    private static final long serialVersionUID = 7747422116792199433L;

    /**
     *
     */
    public GPSdException() {
        super();
    }

    /**
     * @param message the message
     */
    public GPSdException(final String message) {
        super(message);
    }

    /**
     * @param message the message
     * @param cause   the cause
     */
    public GPSdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the cause
     */
    public GPSdException(final Throwable cause) {
        super(cause);
    }
}
