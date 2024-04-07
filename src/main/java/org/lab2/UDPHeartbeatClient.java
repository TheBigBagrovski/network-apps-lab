package org.lab2;

import java.net.*;
import java.util.Random;

public class UDPHeartbeatClient {
    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            Random random = new Random();
            String clientId = String.valueOf(random.nextInt(1000) + 1);

            while (true) {
                // heartbeat
                String heartbeatMessage = "Heartbeat " + clientId + " " + System.currentTimeMillis();
                byte[] sendData = heartbeatMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12000);
                clientSocket.send(sendPacket);
                Thread.sleep(2000);

                // ping
                int sequenceNumber = random.nextInt(1000) + 1;
                String pingMessage = "Ping " + sequenceNumber + " " + System.currentTimeMillis();
                sendData = pingMessage.getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12000);
                clientSocket.send(sendPacket);

                // ожидание ответа
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                long startTime = System.nanoTime();
                clientSocket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                long endTime = System.nanoTime();
                double rtt =  (endTime - startTime) / 1000000.0;
                System.out.println("Ответ от сервера: " + response + " time=" + rtt + " milliseconds");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
