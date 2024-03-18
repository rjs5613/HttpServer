package com.github.rjs5613;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class AsyncHttpServer {

    private final ServerConfig config;
    private final RequestHandlerRegistry registry;
    private final ExecutorService executorService;

    public AsyncHttpServer(ServerConfig config, RequestHandlerRegistry registry, ExecutorService executorService) {
        this.config = config;
        this.registry = registry;
        this.executorService = executorService;
    }

    public void start() throws InterruptedException, IOException {
        AsynchronousServerSocketChannel asyncServer = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(config.port()));
        asyncServer.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Object attachment) {
                asyncServer.accept(null, this);
                executorService.execute(new RequestExecutor(channel, registry, executorService));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        Thread.currentThread().join();
    }

    public void register(String path, HttpMethod httpMethod, Function<HttpRequest, ResponseEntity<?>> requestHandler) {
        registry.register(new HttpRequest(httpMethod, path), requestHandler);
    }
}
