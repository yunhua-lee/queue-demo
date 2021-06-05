package com.example.queuedemo.client;

import com.example.queuedemo.transport.TLVData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/5
 */
public class QueueClientHandler extends SimpleChannelInboundHandler<TLVData> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TLVData TLVData) throws Exception {
		if(TLVData == null){
			throw new Exception("invalid null response");
		}

		if(TLVData.getCmdCode() == '2'){  //request cmd: odd，response：even
			System.out.println("Received pub response: " + TLVData.getBody());
		}else if(TLVData.getCmdCode() == '4'){
			System.out.println("Received pull response: " + TLVData.getBody());
		}else {
			throw new Exception("invalid message, cmd: " + TLVData.getCmdCode());
		}
	}
}
