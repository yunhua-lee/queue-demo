package com.example.queuedemo.transport;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class TLVData{
    public static final int MAX_FRAME_LENGTH = 1024 * 1024;
    public static final int LENGTH_FIELD_LENGTH = 4;
    public static final int LENGTH_FIELD_OFFSET = 1;
    public static final int LENGTH_ADJUSTMENT = 0;
    public static final int INITIAL_BYTES_TO_STRIP = 0;

    private final byte cmd;
    private final int length;
    private final String body;

    public TLVData(byte cmd, int length, String body) {
        this.cmd = cmd;
        this.length = length;
        this.body = body;
    }

    public byte getCmdCode() {
        return cmd;
    }

    public int getBodyLength() {
        return length;
    }

    public String getBody() {
        return body;
    }
}
