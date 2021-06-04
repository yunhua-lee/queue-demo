package com.example.queuedemo.transport;

import com.example.queuedemo.role.Role;
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
			Response response = role.pub(request);
			channelHandlerContext.writeAndFlush(response);
			return;
		}

		if( o instanceof PullRequest){
			PullRequest request = (PullRequest)o;
			//具体的业务处理代码，此处简化
			Response response = role.pull(request);
			channelHandlerContext.writeAndFlush(response);

			return;
		}

		throw new Exception("Unknown request!");
	}
}
