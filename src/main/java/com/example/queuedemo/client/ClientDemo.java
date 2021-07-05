package com.example.queuedemo.client;

import com.example.queuedemo.transport.TLVData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/5
 */
public class ClientDemo {
    public static void main(String[] args) throws Exception {
        final Options options = new Options();
        options.addOption("s",true, "server ip");
        options.addOption("p",true, "server port");

        final CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new Exception("parser command line error",e);
        }
        String server = "127.0.0.1";
        if(commandLine.hasOption('s')) {
            server = commandLine.getOptionValue('s');
        }

        int port = 8080;
        if(commandLine.hasOption('p')) {
            port = Integer.valueOf(commandLine.getOptionValue('p'));
        }

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new QueueClientInitializer());

        try {
            ChannelFuture future = bootstrap.connect(server,port).sync();

            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                TLVData request;
                String[] cmd = line.split(" ");
                if( "pub".equalsIgnoreCase(cmd[0])){
                    if(cmd.length != 2){
                        System.out.println("invalid command, example: \"pub hello\"");
                        continue;
                    }
                    request = new TLVData((byte) '1', cmd[1].length(), cmd[1]);
                }else if("pull".equalsIgnoreCase(cmd[0])){
                    if(cmd.length != 1){
                        System.out.println("invalid command, example: \"pull\"");
                        continue;
                    }
                    request = new TLVData((byte) '3', 1, "1");
                }else if("bye".equalsIgnoreCase(cmd[0])) {
                    break;
                }else {
                    System.out.println("invalid command, example: \"pub hello\", or \"pull\"");
                    continue;
                }

                lastWriteFuture = future.channel().writeAndFlush(request);
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
            future.channel().close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
    private static void usage(){
        System.out.println("Usage: \n java -jar client-demo.jar  -s {server ip} -p {port}");
    }

}
