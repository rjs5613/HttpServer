package com.github.rjs5613;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Function;

public class RequestExecutor {

    private final AsynchronousSocketChannel channel;
    private final RequestHandlerRegistry registry;

    public RequestExecutor(AsynchronousSocketChannel channel, RequestHandlerRegistry registry) {
        this.channel = channel;
        this.registry = registry;
    }

    public void execute() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, null, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, Object attachment) {
                ResponseEntity<?> responseEntity;
                try {

                    if (handleEmpty(result)) return;
                    HttpRequest request = new HttpRequest(buffer);
                    System.out.println(request);
                    Thread.sleep(10000);
                    Function<HttpRequest, ResponseEntity<?>> handler = registry.handlerFor(request);
                    responseEntity = handler.apply(request);
                } catch (Exception e) {
                    if (e instanceof HttpError error) {
                        responseEntity = new ResponseEntity<>(error.message(), error.status());
                    } else {
                        responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                new HttpResponse(responseEntity, channel).send();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                ResponseEntity<String> responseEntity = new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                new HttpResponse(responseEntity, channel).send();
            }
        });
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
