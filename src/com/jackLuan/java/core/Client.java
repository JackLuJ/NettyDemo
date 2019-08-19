package com.jackLuan.java.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.ocsp.OcspClientHandler;

public class Client {

    private static String Host = System.getProperty("host", "127.0.0.1");

    private static int POTR = Integer.parseInt(System.getProperty("port", "8765"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup workgroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workgroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf = bootstrap.connect(Host, POTR).sync();

        cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));
        cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello world".getBytes()));

        cf.channel().closeFuture().sync();

        workgroup.shutdownGracefully();
    }

}
