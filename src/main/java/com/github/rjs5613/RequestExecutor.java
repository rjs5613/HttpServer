package com.github.rjs5613;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Function;

public class RequestExecutor {

    private final AsynchronousSocketChannel channel;

    public RequestExecutor(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void execute() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, null, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, Object attachment) {
                if (handleEmpty(result)) return;
                HttpRequest request = new HttpRequest(buffer);
                ResponseEntity<?> responseEntity;
                try {
                    Function<HttpRequest, ResponseEntity<?>> handler = RequestHandlerRegistry.instance().handlerFor(request);
                    responseEntity = handler.apply(request);
                } catch (Exception e) {
                    ResponseEntity<String> response;
                    if (e instanceof HttpError error) {
                        responseEntity = new ResponseEntity<>(error.getMessage(), error.status());
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
