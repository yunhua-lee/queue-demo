package com.example.queuedemo.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueServerEncoder extends MessageToByteEncoder<Response> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Response response, ByteBuf byteBuf) throws Exception {
		byteBuf.writeByte(response.getCmd());
		byteBuf.writeInt(response.getLength());
		byteBuf.writeBytes(response.getBody().getBytes(Charset.forName("utf-8")));
	}
}
