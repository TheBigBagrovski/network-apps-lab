package org.lab2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UDPClient2 {
    public static void main(String[] args) {
        DatagramSocket clientSocket;
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(1000);
            InetAddress serverAddress = InetAddress.getByName("localhost");
            byte[] sendData;
            byte[] receiveData = new byte[1024];
            List<Double> rttTimes = new ArrayList<>();
            int lostPackets = 0;

            for (int sequenceNumber = 1; sequenceNumber <= 10; sequenceNumber++) {
                String message = "Ping " + sequenceNumber + " " + System.currentTimeMillis();
                sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12000);
                clientSocket.send(sendPacket);
                long startTime = System.nanoTime();
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    clientSocket.receive(receivePacket);
                    long endTime = System.nanoTime();
                    double rtt = (endTime - startTime) / 1000000.0;
                    rttTimes.add(rtt);
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Response from " + receivePacket.getAddress() + ": " + response + " time=" + rtt + " milliseconds");
                } catch (SocketTimeoutException e) {
                    lostPackets++;
                    System.out.println("Request timed out");
                }
            }
            clientSocket.close();

            if (!rttTimes.isEmpty()) {
                Collections.sort(rttTimes);
                double minRtt = rttTimes.get(0);
                double maxRtt = rttTimes.get(rttTimes.size() - 1);
                double totalRtt = 0;
                for (double rtt : rttTimes) {
                    totalRtt += rtt;
                }
                double avgRtt = totalRtt / rttTimes.size();
                double packetLoss = (double) lostPackets / 10 * 100;

                System.out.println("--- Ping statistics ---");
                System.out.println("10 packets transmitted, " + rttTimes.size() + " packets received, " + String.format("%.1f", packetLoss) + "% packet loss");
                System.out.println("Min/Max/Avg RTT = " + minRtt + "/" + maxRtt + "/" + avgRtt + " milliseconds");
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
