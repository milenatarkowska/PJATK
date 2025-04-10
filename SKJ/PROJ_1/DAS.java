import java.io.*;
import java.net.*;
import java.util.*;

public class DAS {
    public static void main(String[] args) throws Exception {

        if(args.length != 2){
            System.err.println("Wrong arguments inserted.");
            return;
        } else {
            System.out.println("2 correct arguments inserted.");
        }

        int port = Integer.parseInt(args[0]);
        int number = Integer.parseInt(args[1]);

        if (port < 1024 || port > 65535) {
            System.err.println("Invalid port. Port must be between 1024 and 65535.");
            return;
        }

        try {
            modeMaster(port, number);
        } catch (IOException ignored) {
            modeSlave(port, number);
        }
    }

    public static void modeMaster(int port, int number) throws Exception{
        Log.PREFIX = "";
        Log.log("Master mode entered");
        Log.PREFIX = "MASTER";

        Log.log("Opening socket");
        DatagramSocket masterSocket = new DatagramSocket(port);
        masterSocket.setBroadcast(true);
        InetAddress broadcastAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses().stream().filter(interfaceAddress -> interfaceAddress.getBroadcast() != null).findFirst().get().getBroadcast();
        Log.log("Socket opened");

        List<Integer> numData = new ArrayList<>();
        numData.add(number);
        Log.log("Number added: " + number);

        byte[] buffer = new byte[1400];
        DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
        boolean isWorking = true;

        while(isWorking){
            Log.log("Waiting for packages...");
            masterSocket.receive(packetReceived);


            if(packetReceived.getAddress().getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())){
                Log.log("Skipping packet from myself");
                continue;
            }

            String receivedNumber = new String(packetReceived.getData(), 0, packetReceived.getLength());
            if (isNumberInteger(receivedNumber)) {
                int receivedInt = Integer.parseInt(receivedNumber);

                switch (receivedInt) {
                    case 0:
                        String response = String.valueOf((int) numData.stream().mapToInt(Integer::intValue).average().getAsDouble());
                        sendResponse(masterSocket, response, packetReceived.getAddress(), packetReceived.getPort());
                        if(broadcastAddress == null){
                            Log.log("Cannot send broadcast response");
                        } else {
                        sendResponse(masterSocket, response, broadcastAddress, 60000);
                        }
                        break;

                    case -1:
                        sendResponse(masterSocket, receivedNumber, packetReceived.getAddress(), packetReceived.getPort());
                        if(broadcastAddress == null){
                            Log.log("Cannot send broadcast message");
                        } else {
                            sendResponse(masterSocket, receivedNumber, broadcastAddress, port);
                        }
                        Log.log("Closing Master");
                        masterSocket.close();
                        isWorking = false;
                        break;

                    default:
                            Log.log("Number added: " + receivedNumber);
                            if(packetReceived.getAddress().getHostAddress().equals("127.0.0.1")){
                                sendResponse(masterSocket, receivedNumber, packetReceived.getAddress(), packetReceived.getPort());
                            }
                            numData.add(receivedInt);
                        break;
                }
            }
        }
    }

    public static void modeSlave(int port, Integer number) throws Exception{
        Log.PREFIX = "";
        Log.log("Slave mode entered");
        Log.PREFIX = "SLAVE";

        Log.log("Opening socket");
        DatagramSocket slaveSocket = new DatagramSocket();
        slaveSocket.setSoTimeout(5000);
        Log.log("Socket opened on port: " + slaveSocket.getLocalPort());

        Log.log("Creating byte array with data to send");
        byte[] buffer = String.valueOf(number).getBytes();
        Log.log("Array created");

        InetAddress receiverIP = InetAddress.getLoopbackAddress();

        Log.log("Creating packet from byte data");
        DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, receiverIP, port);
        Log.log("Packet created");

        Log.log("Sending packet");
        slaveSocket.send(packetToSend);
        Log.log("Packet sent");

        byte[] responseBuf = new byte[1400];
        DatagramPacket responsePacket = new DatagramPacket(responseBuf, responseBuf.length);
        slaveSocket.receive(responsePacket);
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
        Log.log("Response from master: " + response);

        slaveSocket.close();

        Log.log("Closing slave socket");
    }

    public static boolean isNumberInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void sendResponse(DatagramSocket socket, String response, InetAddress address, int port) throws IOException {
        byte[] sendBuffer = response.getBytes();
        DatagramPacket packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        Log.log("Sending response to "+ address +" : " + response);
        socket.send(packetToSend);
    }
}

class Log {
    public static String PREFIX = "";
    private static final String LOG_FILE = "log.txt";

    public static void log(String message) {
        String fullMessage = "[" + PREFIX + "] " + message;
        System.out.println(fullMessage);
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(fullMessage);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}