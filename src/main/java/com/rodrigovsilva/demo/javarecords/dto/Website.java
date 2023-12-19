package com.rodrigovsilva.demo.javarecords.dto;

import java.net.URI;

public record Website(URI url, String description) {

    public Website {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
    }

    // Additional constructor to convert String to URI
    public Website(String url, String description) {
        this(URI.create(url), description);
    }
}
