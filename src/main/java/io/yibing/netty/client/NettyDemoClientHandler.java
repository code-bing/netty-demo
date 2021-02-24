package io.yibing.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyDemoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }
}
