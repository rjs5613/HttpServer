package com.github.rjs5613;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class RequestExecutor implements Runnable {

    private final AsynchronousSocketChannel channel;
    private final RequestHandlerRegistry registry;
    private final ExecutorService executorService;

    public RequestExecutor(AsynchronousSocketChannel channel, RequestHandlerRegistry registry, ExecutorService executorService) {
        this.channel = channel;
        this.registry = registry;
        this.executorService = executorService;
    }

    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, null, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, Object attachment) {
                executorService.execute(new RequestWorker(channel, registry, buffer, result));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                ResponseEntity<String> responseEntity = new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                new HttpResponse(responseEntity, channel).send();
            }
        });
    }

    public static class RequestWorker implements Runnable {

        private final ByteBuffer buffer;
        private final Integer result;
        private final RequestHandlerRegistry registry;
        private final AsynchronousSocketChannel channel;

        public RequestWorker(AsynchronousSocketChannel channel, RequestHandlerRegistry registry, ByteBuffer buffer, Integer result) {
            this.buffer = buffer;
            this.result = result;
            this.registry = registry;
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                if (handleEmpty(result)) return;
                HttpRequest request = new HttpRequest(buffer);
                System.out.println(Thread.currentThread() + "   -  " + request);
                Thread.sleep(10000);
                Function<HttpRequest, ResponseEntity<?>> handler = registry.handlerFor(request);
                new HttpResponse(handler.apply(request), channel).send();
            } catch (Exception e) {
                ResponseEntity<?> responseEntity;
                if (e instanceof HttpError error) {
                    responseEntity = new ResponseEntity<>(error.message(), error.status());
                } else {
                    responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                new HttpResponse(responseEntity, channel).send();
            }
        }

        private boolean handleEmpty(Integer result) {
            if (result == -1) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }
}
