package org.lab4;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ProxyServer {

    public static void main(String[] args) {
        int port = 8080;
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера на порту " + port);
            return;
        }
        server.createContext("/", new ProxyHandler(port));
        server.start();
        System.out.println("Сервер запущен на порту " + port);
    }

}