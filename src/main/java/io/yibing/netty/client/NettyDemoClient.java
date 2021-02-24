package io.yibing.netty.client;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class NettyDemoClient {
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new NettyDemoClientHandler());
                }
            });
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8083).sync();
            System.out.println("已连接");
            Channel channel = channelFuture.channel();
            while (true){
                Scanner scanner = new Scanner(System.in);
                System.out.print("请输入：");
                String msg = scanner.nextLine();
                if("exit".equals(msg)){
                    break;
                }
                channel.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
            }
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();

        }

    }
}
