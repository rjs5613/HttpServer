package com.github.rjs5613;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RequestHandlerRegistry {


    private final Map<HttpRequest, Function<HttpRequest, ResponseEntity<?>>> requestHandlers;

    public RequestHandlerRegistry() {
        this.requestHandlers = new ConcurrentHashMap<>();
    }

    public void register(HttpRequest request, Function<HttpRequest, ResponseEntity<?>> handler) {
        if (Objects.nonNull(request) && Objects.nonNull(handler)) {
            requestHandlers.put(request, handler);
        }
    }

    public Function<HttpRequest, ResponseEntity<?>> handlerFor(HttpRequest request) throws NotFoundError {
        if (requestHandlers.containsKey(request)) {
            return requestHandlers.get(request);
        }
        throw new NotFoundError("No Handler Registered!!");
    }

}
