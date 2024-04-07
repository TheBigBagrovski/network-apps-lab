package org.lab2;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDPHeartbeatServer {
    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(12000)) {
            byte[] receiveData = new byte[1024];
            Map<String, Long> clients = new HashMap<>();

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] messageData = message.split(" ");

                if (messageData.length == 3 && messageData[0].equals("Heartbeat")) {
                    String clientId = messageData[1];
                    clients.put(clientId, System.currentTimeMillis());
                    System.out.println("Heartbeat получен от клиента " + clientId);

                    clients.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() > 5000);
                    clients.forEach((key, value) -> {
                        if (System.currentTimeMillis() - value > 5000) {
                            System.out.println("Клиент " + key + " неактивен.");
                        }
                    });
                }

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                serverSocket.send(new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), clientAddress, clientPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
