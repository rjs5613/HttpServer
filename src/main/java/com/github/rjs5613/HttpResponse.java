package com.github.rjs5613;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class HttpResponse {
    private final ResponseEntity<?> responseEntity;
    private final AsynchronousSocketChannel channel;

    public HttpResponse(ResponseEntity<?> responseEntity, AsynchronousSocketChannel channel) {
        this.responseEntity = responseEntity;
        this.channel = channel;
    }

    public void send() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String response = responseEntity.asString();
        channel.write(ByteBuffer.wrap(response.getBytes()), null, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, Object attachment) {
                buffer.clear();
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }
}
