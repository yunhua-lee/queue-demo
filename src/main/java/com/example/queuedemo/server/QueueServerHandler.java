package com.example.queuedemo.server;

import com.example.queuedemo.role.Role;
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
		if(o == null){
			System.err.println("Received null request.");
			return;
		}

		if(o instanceof TLVData ){
			TLVData data = (TLVData) o;
			if(data.getCmdCode() == '1'){  //request cmd: odd，response：even
				PubRequest request = new PubRequest(data.getBody());

				//具体的业务处理代码，此处简化
				TLVData response = role.pub(request);
				channelHandlerContext.writeAndFlush(response);

				return;
			}else if(data.getCmdCode() == '3'){
				PullRequest request = new PullRequest(data.getBody());

				//具体的业务处理代码，此处简化
				TLVData response = role.pull(request);
				channelHandlerContext.writeAndFlush(response);

				return;
			}else {
				throw new Exception("invalid message, cmd: " + data.getCmdCode());
			}
		}

		throw new Exception("Unknown request!");
	}
}
