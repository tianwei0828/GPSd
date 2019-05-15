package com.tw.gpsd.commands;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public interface Command {
    String KEY_CLASS = "class";

    String toJson();

    String toLine();
}
