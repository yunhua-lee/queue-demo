package com.example.queuedemo;

import com.example.queuedemo.role.Master;
import com.example.queuedemo.role.Role;
import com.example.queuedemo.role.Slave;
import com.example.queuedemo.server.QueueServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.cli.*;

/**
 * @Desctiption SpringBoot Application
 * @Author wallace
 * @Date 2021/6/4
 */
public class Application {
	public static void main(String args[]) throws Exception{
		System.out.println("Welcome to queue system!");

		CommandLine cmd = parseOptions(args, buildOptions());

		//parse options
		String systemName = "";
		if(cmd.hasOption("S")) {
			systemName = cmd.getOptionValue('S');
		}else{
			System.err.println("please input system name by -S option");
			usage();
			System.exit(1);
		}

		String groupName = "";
		if(cmd.hasOption("g")) {
			groupName = cmd.getOptionValue('g');
		}else{
			System.err.println("please input group name by -g option");
			usage();
			System.exit(1);
		}

		String zkAddr = "";
		if(cmd.hasOption('z')) {
			zkAddr = cmd.getOptionValue('z');
		}else{
			System.err.println("please input the ZooKeeper address by -z option");
			usage();
			System.exit(1);
		}

		int port = 8080;
		if(cmd.hasOption('p')) {
			port = Integer.valueOf(cmd.getOptionValue('p'));
		}

		//build role
		Role role;
		if(cmd.hasOption('m')) {
			role = new Master(systemName, groupName, zkAddr);
		}else if(cmd.hasOption('s')){
			role = new Slave(systemName, groupName, zkAddr);
		}else{
			System.err.println("please specific the role use -m (master) or -s (slave)");
			usage();
			System.exit(1);
			return;
		}

		role.start();

		//build netty server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(8);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new QueueServerInitializer(role));

			Channel ch = b.bind(port).sync().channel();
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private static Options buildOptions(){
		final Options options = new Options();

		options.addOption("p", true, "server port");       //port
		options.addOption("m", "node is master");                 //master
		options.addOption("s", "node is slave");                  //slave
		options.addOption("z", true, "ZooKeeper address"); //zk address
		options.addOption("S", true, "system name");       //system name
		options.addOption("g", true, "group name");        //group name

		return options;
	}

	private static CommandLine parseOptions(String args[], Options options) throws Exception {
		final CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			throw new Exception("parser command line error",e);
		}

		return cmd;
	}

	private static void usage(){
		System.out.println("Usage: \n java -jar queueDemo.jar -p {port} -S {system name} -g {group name} -z {zookeeper address} -m/-s (master/slave) ");
	}

}
