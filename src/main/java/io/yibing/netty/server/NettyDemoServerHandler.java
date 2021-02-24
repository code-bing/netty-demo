package io.yibing.netty.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyDemoServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        String resp;
        for (Channel ch : channelGroup) {
            if (channel != ch) {
                resp = "【" + channel.remoteAddress() + "】说:" + msg;
            } else {
                resp = "【你自己】说:" + msg;
            }
            ch.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes(CharsetUtil.UTF_8)));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
        String resp = ctx.channel().remoteAddress() + " 进入聊天室";
        channelGroup.forEach(item ->{
            if(item != ctx.channel()){
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes(CharsetUtil.UTF_8)));
            }
        });
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("unregistered............");

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("register...........");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active................");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
        String resp = ctx.channel().remoteAddress() + " 下线了";
        channelGroup.remove(ctx.channel());
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes(CharsetUtil.UTF_8)));
    }
}
