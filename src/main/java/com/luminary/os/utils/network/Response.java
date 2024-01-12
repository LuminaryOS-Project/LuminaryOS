package com.luminary.os.utils.network;

import lombok.Getter;

import java.util.Map;

@Getter
public class Response {
    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;

    public Response(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
