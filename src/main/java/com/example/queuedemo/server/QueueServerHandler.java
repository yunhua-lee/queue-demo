package com.example.queuedemo.server;

import com.example.queuedemo.role.Role;
import com.example.queuedemo.transport.PubRequest;
import com.example.queuedemo.transport.PullRequest;
import com.example.queuedemo.transport.TLVData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class QueueServerHandler extends SimpleChannelInboundHandler<Object> {
	private final Role role;

	public QueueServerHandler(Role role){
		this.role = role;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
		if( o instanceof PubRequest){
			PubRequest request = (PubRequest)o;
			//具体的业务处理代码，此处简化
			TLVData data = role.pub(request);
			channelHandlerContext.writeAndFlush(data);
			return;
		}

		if( o instanceof PullRequest){
			PullRequest request = (PullRequest)o;

			//具体的业务处理代码，此处简化
			TLVData data = role.pull(request);
			channelHandlerContext.writeAndFlush(data);
			return;
		}

		throw new Exception("Unknown request!");
	}
}
