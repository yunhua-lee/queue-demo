package com.example.queuedemo.server;

import com.example.queuedemo.role.Role;
import com.example.queuedemo.transport.TLVData;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueServerInitializer extends ChannelInitializer<SocketChannel> {


	private final Role role;

	public QueueServerInitializer(Role role){
		this.role = role;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline p = socketChannel.pipeline();

		p.addLast(new QueueServerDecoder(TLVData.MAX_FRAME_LENGTH, TLVData.LENGTH_FIELD_LENGTH,
				TLVData.LENGTH_FIELD_OFFSET, TLVData.LENGTH_ADJUSTMENT, TLVData.INITIAL_BYTES_TO_STRIP));
		p.addLast(new QueueServerEncoder());
		p.addLast(new QueueServerHandler(role));
	}
}
