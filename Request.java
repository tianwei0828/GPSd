package com.tw.gpsd;

import com.tw.gpsd.commands.Command;

/**
 * Created by wei.tian
 * 2019/4/16
 */
public interface Request {
    Command command();

    void send();
}
