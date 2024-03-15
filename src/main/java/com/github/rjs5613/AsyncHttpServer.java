package com.github.rjs5613;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Function;

public class AsyncHttpServer {

    private final ServerConfig config;

    public AsyncHttpServer(ServerConfig config) {
        this.config = config;
    }

    public void start() throws InterruptedException, IOException {
        AsynchronousServerSocketChannel asyncServer = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(config.port()));
        asyncServer.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Object attachment) {
                asyncServer.accept(null, this);
                new RequestExecutor(channel).execute();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        Thread.currentThread().join();
    }

    public void register(String path, HttpMethod httpMethod, Function<HttpRequest, ResponseEntity<?>> requestHandler) {
        RequestHandlerRegistry.instance().register(new HttpRequest(httpMethod, path), requestHandler);
    }
}
