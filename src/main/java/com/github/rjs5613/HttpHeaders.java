package com.github.rjs5613;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpHeaders {

    private final String name;
    private final Set<String> values;

    private HttpHeaders(String name, Set<String> values) {
        this.name = name;
        this.values = values;
    }

    public static HttpHeaders from(String headerString) {
        String[] split = headerString.split(":");
        Set<String> values = Stream.of(split[1].split(",")).map(String::trim).collect(Collectors.toSet());
        return new HttpHeaders(split[0], values);
    }

    public String getName() {
        return name;
    }

    public Set<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
