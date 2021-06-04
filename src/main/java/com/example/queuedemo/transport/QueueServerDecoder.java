package com.example.queuedemo.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueServerDecoder extends LengthFieldBasedFrameDecoder {

	private static final int HEADER_SIZE = 5;
	private byte cmd;
	private int length;
	private String msgBody;

	public QueueServerDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if(in == null){
			return null;
		}

		if(in.readableBytes() < HEADER_SIZE){
			throw new Exception("invalid message");
		}

		cmd = in.readByte();
		length = in.readInt(); //readByte ?

		if(in.readableBytes() < length){
			throw new Exception("invalid message, required length:" + length
					+ ", real length: " + in.readableBytes() );
		}

		byte []bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		String body = new String(bytes,"UTF-8");

		if(cmd == '1'){
			return new PubRequest(body);
		}else if(cmd == '3'){
			return new PullRequest(body);
		}else {
			throw new Exception("invalid message, cmd: " + cmd);
		}
	}
}
