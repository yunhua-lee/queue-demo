package com.example.queuedemo.client;

import com.example.queuedemo.transport.TLVData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueClientDecoder extends LengthFieldBasedFrameDecoder {

	private static final int HEADER_SIZE = 5;

	private byte cmd;
	private int length;
	private String msgBody;

	public QueueClientDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
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
		length = in.readInt();

		if(in.readableBytes() < length){
			throw new Exception("invalid message, required length:" + length
					+ ", real length: " + in.readableBytes() );
		}

		byte []bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		String body = new String(bytes,"UTF-8");

		return new TLVData(cmd, length, body);
	}
}