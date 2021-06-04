package com.example.queuedemo.transport;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class Response {
	private byte cmd;
	private int length;
	private String body;

	public Response(byte cmd, int length, String body) {
		this.cmd = cmd;
		this.length = length;
		this.body = body;
	}

	public byte getCmd() {
		return cmd;
	}

	public int getLength() {
		return length;
	}

	public String getBody() {
		return body;
	}
}
