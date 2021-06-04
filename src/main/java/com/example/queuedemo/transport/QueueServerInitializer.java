package com.example.queuedemo.transport;

import com.example.queuedemo.role.Role;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final int MAX_FRAME_LENGTH = 1024 * 1024;
	private static final int LENGTH_FIELD_LENGTH = 4;
	private static final int LENGTH_FIELD_OFFSET = 1;
	private static final int LENGTH_ADJUSTMENT = 0;
	private static final int INITIAL_BYTES_TO_STRIP = 0;

	private final Role role;

	public QueueServerInitializer(Role role){
		this.role = role;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline p = socketChannel.pipeline();

		p.addLast(new QueueServerDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_LENGTH,
				LENGTH_FIELD_OFFSET, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
		p.addLast(new QueueServerHandler(role));
		p.addLast(new QueueServerEncoder());
	}
}
