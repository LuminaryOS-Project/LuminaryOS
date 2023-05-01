package com.luminary.os.utils;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Requests {
    public static String get(String url) throws IOException {
        return request("GET", url, null, null);
    }

    public static String get(String url, Map<String, String> params) throws IOException {
        return request("GET", url, params, null);
    }

    public static String post(String url, Map<String, String> data) throws IOException {
        return request("POST", url, null, data);
    }
    public static void download(String url, String path) throws IOException {
        URL u = new URL(url);
        InputStream in = u.openStream();
        OutputStream out = new FileOutputStream(path);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();
    }

    public static String post(String url, Map<String, String> params, Map<String, String> data) throws IOException {
        return request("POST", url, params, data);
    }

    public static Map<String, Object> json(String url) throws IOException {
        String response = request("GET", url, null, null);
        Gson gson = new Gson();
        return gson.fromJson(response, HashMap.class);
    }

    private static String request(String method, String url, Map<String, String> params, Map<String, String> data) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod(method);

        if (params != null) {
            String query = buildQueryString(params);
            conn.setDoOutput(true);
            conn.getOutputStream().write(query.getBytes());
        }

        if (data != null) {
            String query = buildQueryString(data);
            conn.setDoOutput(true);
            conn.getOutputStream().write(query.getBytes());
        }

        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }
        }

        return responseBuilder.toString();
    }

    private static String buildQueryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
}