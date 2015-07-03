package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.model.rpc.methods.Request;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class Client {

    private static final String RPC_URL = "http://%s:%s/transmission/rpc";
    private static Client instance;
    private HttpClient client;
    private String server;
    private Integer port;
    private String username;
    private String password;
    private String csrf = null;

    private Client(String server, Integer port, String username, String password) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;

        CredentialsProvider cred = new BasicCredentialsProvider();
        cred.setCredentials(new AuthScope(server, port), new UsernamePasswordCredentials(username, password));
        client = HttpClients.custom()
                .setDefaultCredentialsProvider(cred)
                .build();

        HttpPost request = new HttpPost(String.format(RPC_URL, server, port));
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT)
                csrf = response.getFirstHeader("X-Transmission-Session-Id").getValue();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            request.releaseConnection();
        }
    }

    public static Client getInstance(String server, Integer port, String username, String password) {
        instance = new Client(server, port, username, password);
        return instance;
    }

    public static Client getInstance() {
        return instance;
    }

    /*
    private boolean isAlive() {
        try {
            URL url = new URL(String.format(RPC_URL, server, port));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.connect();
            int code = conn.getResponseCode();
            return code == HttpStatus.SC_UNAUTHORIZED
                    || code == HttpStatus.SC_OK
                    || code == HttpStatus.SC_CONFLICT;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public boolean isConnectionAvailable() {
        if (!isAlive())
            return false;

        HttpPost request = new HttpPost(String.format(RPC_URL, server, port));
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT)
                csrf = response.getFirstHeader("X-Transmission-Session-Id").getValue();
            return response.getStatusLine().getStatusCode() != HttpStatus.SC_UNAUTHORIZED;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            request.releaseConnection();
        }
        return false;
    }
    */

    public String getServer() {
        return server;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    Request send(Request request) {
        /*
        if (!isConnectionAvailable())
            return request.fromJson("{\"result\":\"error\"}");
            */

        HttpPost req = new HttpPost(String.format(RPC_URL, server, port));
        req.addHeader("X-Transmission-Session-Id", csrf);
        req.addHeader("Accept-Encoding", "gzip,deflate");
        StringBuilder builder = new StringBuilder();
        try {
            HttpEntity reqEntity = new StringEntity(request.toJson(), "utf-8");
            req.setEntity(reqEntity);

            HttpEntity respEntity = client.execute(req).getEntity();

            BufferedReader reader;
            if (respEntity.getContent() instanceof GZIPInputStream) {
                GZIPInputStream gz = (GZIPInputStream) respEntity.getContent();
                reader = new BufferedReader(new InputStreamReader(gz));
            } else
                reader = new BufferedReader(new InputStreamReader(respEntity.getContent()));

            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
            reader.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            req.releaseConnection();
        }

        return request.fromJson(builder.toString());
    }
}