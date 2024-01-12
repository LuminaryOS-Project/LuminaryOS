package com.luminary.os.utils.network;

import com.luminary.os.utils.async.Promise;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class Request {
    public static Promise<Response> get(String url, Map<String, String> headers) {
        return sendRequest("GET", url, headers, null);
    }

    public static Promise<Response> post(String url, Map<String, String> headers, String data) {
        return sendRequest("POST", url, headers, data);
    }

    public static Promise<Response> put(String url, Map<String, String> headers, String data) {
        return sendRequest("PUT", url, headers, data);
    }

    public static Promise<Response> delete(String url, Map<String, String> headers) {
        return sendRequest("DELETE", url, headers, null);
    }
    public static Response download(String url, Map<String, String> headers, String filename) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.connect();
        int statusCode = conn.getResponseCode();
        Map<String, String> responseHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                responseHeaders.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(filename)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return new Response(statusCode, responseHeaders, null);
    }

    private static Promise<Response> sendRequest(String method, String url, Map<String, String> headers, String data) {
        Promise<Response> promise = new Promise<>();
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod(method);
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                if (data != null) {
                    conn.setDoOutput(true);
                    try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                        out.writeBytes(data);
                        out.flush();
                    }
                }
                conn.connect();
                int statusCode = conn.getResponseCode();
                Map<String, String> responseHeaders = new HashMap<>();
                for (Map.Entry<String, java.util.List<String>> entry : conn.getHeaderFields().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().get(0);
                    responseHeaders.put(key, value);
                }
                String responseBody = null;
                if (statusCode == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    responseBody = scanner.useDelimiter("\\A").next();
                    scanner.close();
                }
                Response response = new Response(statusCode, responseHeaders, responseBody);
                promise.resolve(response);
            } catch (Exception e) {
                promise.reject(e);
            }
        }).start();
        return promise;
    }

}