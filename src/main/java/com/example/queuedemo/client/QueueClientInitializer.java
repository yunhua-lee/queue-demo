package com.example.queuedemo.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import static com.example.queuedemo.transport.TLVData.*;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueClientInitializer extends ChannelInitializer<SocketChannel> {
	public QueueClientInitializer(){
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline p = socketChannel.pipeline();

		p.addLast(new QueueClientEncoder());
		p.addLast(new QueueClientDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_LENGTH,
				LENGTH_FIELD_OFFSET, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
		p.addLast(new QueueClientHandler());

	}
}
