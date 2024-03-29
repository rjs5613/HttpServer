package com.github.rjs5613;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record HttpHeader(String name, Set<String> values) {

    public static HttpHeader from(String headerString) {
        String[] split = headerString.split(":");
        Set<String> values = Stream.of(split[1].split(",")).map(String::trim).collect(Collectors.toSet());
        return new HttpHeader(split[0], values);
    }

    public static HttpHeader of(String name, String... value) {
        return new HttpHeader(name, Set.of(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeader that = (HttpHeader) o;
        return Objects.equals(name.toUpperCase(), that.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toUpperCase());
    }
}
