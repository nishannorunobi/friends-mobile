package com.hello.nishan.model;

/**
 * Created by nishan on 2/18/17.
 */
public enum SocketTypeEnum {
    HF(1),AG(2),AG_SERVER(3);

    private int value;
    SocketTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
