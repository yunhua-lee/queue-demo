package com.example.queuedemo.server;

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
public class QueueServerEncoder extends MessageToByteEncoder<TLVData> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, TLVData TLVData, ByteBuf byteBuf) throws Exception {
		System.out.println("Send response: " + TLVData.getBody());

		byteBuf.writeByte(TLVData.getCmdCode());
		byteBuf.writeInt(TLVData.getBodyLength());
		byteBuf.writeBytes(TLVData.getBody().getBytes(Charset.forName("utf-8")));
	}
}
