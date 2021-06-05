package com.example.queuedemo.client;

import com.example.queuedemo.transport.TLVData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueClientEncoder extends MessageToByteEncoder<TLVData> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, TLVData request, ByteBuf byteBuf) throws Exception {
		byteBuf.writeByte(request.getCmdCode());
		byteBuf.writeInt(request.getBodyLength());
		byteBuf.writeBytes(request.getBody().getBytes(Charset.forName("utf-8")));
	}
}
