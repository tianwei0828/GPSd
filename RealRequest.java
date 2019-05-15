package com.tw.gpsd;

import com.tw.gpsd.commands.Command;

/**
 * Created by wei.tian
 * 2019/4/16
 */
public class RealRequest implements Request {
    private final Command command;
    private final GPSdClient gpSdClient;

    public RealRequest(GPSdClient gpSdClient, Command command) {
        this.gpSdClient = gpSdClient;
        this.command = command;
    }

    @Override
    public Command command() {
        return command;
    }

    @Override
    public void send() {
        if (gpSdClient.socketClient != null) {
            gpSdClient.socketClient.write(command.toLine());
        }
    }
}
