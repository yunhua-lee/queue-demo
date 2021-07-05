package com.example.queuedemo.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class TLVEncoder extends MessageToByteEncoder<TLVData> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TLVData data, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(data.getCmdCode());
        byteBuf.writeInt(data.getBodyLength());
        byteBuf.writeBytes(data.getBody().getBytes(StandardCharsets.UTF_8));
    }
}
