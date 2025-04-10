//javac
//jar cfm CCS.jar Manifest.txt *.class

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CCS {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static int clientCounter = 0;
    private static int operationsCounter = 0;
    private static int addCounter = 0;
    private static int subCounter = 0;
    private static int mulCounter = 0;
    private static int divCounter = 0;
    private static int errorCounter = 0;
    private static int sumOfResults = 0;
    private static boolean isUDPRunning = false;
    private static boolean isTCPRunning = false;

    public static void main(String[] args) throws Exception {

        if (!isUDPRunning && !isTCPRunning) {
            switch (args.length) {
                case 1:
                    int port = Integer.parseInt(args[0]);

                    if (port < 1024 || port > 65535) {
                        System.err.println("Invalid port number. Port number must be between 1024 and 65535");
                        return;
                    }

                    try (ServerSocket testSocket = new ServerSocket(port)) {
                        testSocket.close();
                    } catch (IOException e) {
                        System.err.println("Port " + port + " is already in use.");
                        return;
                    }                    

                    new Thread(() -> {
                        try {
                            startUDPServer(port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    isUDPRunning = true;

                    new Thread(() -> {
                        try {
                            startTCPServer(port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    isTCPRunning = true;

                    new Thread(CCS::reportStats).start();
                    break;
                    /*
                case 5:
                    try {
                        startClient(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                    } catch (InputMismatchException e1) {
                        System.out.println("Try typing java -jar CCS.jar <client_port> <server_port> <oeration> <arg1> <arg2> or <server_port> for");
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        System.out.println("Array out of bounds - use 5 arguments: <client_port> <server_port> <oeration> <arg1> <arg2> for client or 1 <server_port> for server");
                    } catch (NumberFormatException e3) {
                        System.out.println("Use int only in 2 last arguments for client");
                    }
                    break;
                    */
                default:
                    System.err.println("Incorrect number of arguments");
            }
        }
    }

    private static void startUDPServer(int port) throws Exception {
        Log.PREFIX = "UDP";
        Log.log("Opening UDP port on:" + port);

        try {
        DatagramSocket ccsSocket = new DatagramSocket(port);
        byte[] buff = new byte[1024];
        boolean isWorking = true;

        while (isWorking) {
            Log.log("Listening on port:" + port);
            DatagramPacket receivedPacket = new DatagramPacket(buff, buff.length);

            ccsSocket.receive(receivedPacket);
            Log.log("Packet received, verification...");

            String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength(), StandardCharsets.UTF_8);

            if (receivedMessage.startsWith("CCS DISCOVER")) {
                Log.log("Received message is correct, sending response...");

                String responseMessage = "CCS FOUND";
                byte[] responseData = responseMessage.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, receivedPacket.getAddress(), receivedPacket.getPort());
                ccsSocket.send(responsePacket);
                Log.log("Response sent");
            } else {
                Log.log("Message is not correct.");
            }
        }
        }  catch (IOException e) {
            Log.log("Error during communication: " + e.getMessage());
        }
    
    }

    private static void startTCPServer(int port) throws Exception {
        Log.PREFIX = "TCP";
        Log.log("Opening serverSocket");

        ServerSocket serverSocket = new ServerSocket(port);
        boolean isServerWorking = true;

        Log.log("Waiting for clients...");

        while (isServerWorking) {
            Socket clientSocket = serverSocket.accept();
            clientCounter++;

            executor.submit(() -> {
                try {
                    handleClient(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // TESTOWY KLIENT
    /* 
    private static void startClient(int port, int serverPort, String command, int arg1, int arg2) throws Exception {
        Log.PREFIX = "CLIENT";
        Log.log("Opening clientSocket");
        DatagramSocket clientSocket = new DatagramSocket(port);
        String responseMessage = "CCS DISCOVER";
        byte[] responseData = responseMessage.getBytes();
        try {
            InetAddress broadcastAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses().stream().filter(interfaceAddress -> interfaceAddress.getBroadcast() != null).findFirst().map(InterfaceAddress::getBroadcast).orElse(null);
            if (broadcastAddress == null) {
                broadcastAddress = NetworkInterface.getNetworkInterfaces().nextElement().getInterfaceAddresses().stream().filter(interfaceAddress -> interfaceAddress.getBroadcast() != null).findFirst().map(InterfaceAddress::getBroadcast).orElse(null);
            }
            //InetAddress broadcastAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses().stream().filter(interfaceAddress -> interfaceAddress.getBroadcast() != null).findFirst().get().getBroadcast();
            System.out.println(broadcastAddress);
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, broadcastAddress, serverPort);
            clientSocket.send(responsePacket);
            Log.log("CCS DISCOVER sent");
        } catch (IOException e) {
            System.out.println("No broadcats adress found for the current network interface");
        }

        byte[] buff = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(buff, buff.length);

        clientSocket.receive(receivedPacket);
        System.out.println(receivedPacket.getAddress());
        clientSocket.close();


        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(receivedPacket.getAddress(), serverPort), 5000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(command + " " + arg1 + " " + arg2);
        System.out.println(in.readLine());
//        Thread.sleep(10000);
//        out.println(command + " " + arg1 + " " + arg2);
//        System.out.println(in.readLine());
        socket.close();

    }
        */

    private static void handleClient(Socket clientSocket) throws Exception {
        Log.PREFIX = "HANDLE CLIENT";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
   
       Log.log("Connection with client successful");
       String inputData;
   
       while ((inputData = in.readLine()) != null && !inputData.trim().isEmpty()) {
           String[] commandParts = inputData.split(" ");
           if (commandParts.length != 3) {
               errorCounter++;
               Log.log("Error! Bad number of arguments");
               out.println("ERROR");
               continue;
           }
   
           try {
               String operation = commandParts[0];
               int arg1 = Integer.parseInt(commandParts[1]);
               int arg2 = Integer.parseInt(commandParts[2]);
   
               String result = calculate(operation, arg1, arg2);
               operationsCounter++;
   
               try {
                   Log.log("Operation: " + operation + " Result: " + result);
                   sumOfResults += Integer.parseInt(result);
               } catch (NumberFormatException e) {
                   errorCounter++;
               }
   
               out.println(result);
           } catch (NumberFormatException e) {
               Log.log("Error. Number format exception.");
               errorCounter++;
               out.println("ERROR");
           }
       }
   } catch (IOException e) {
       Log.log("Error during client communication: " + e.getMessage());
   } finally {
       try {
           clientSocket.close();
       } catch (IOException e) {
           Log.log("Failed to close client socket: " + e.getMessage());
       }
   }
   
    }

    private static String calculate(String operation, int arg1, int arg2) {
        Log.PREFIX = "CALCULATOR";

        switch (operation) {
            case "ADD":
                addCounter++;
                return String.valueOf(arg1 + arg2);
            case "SUB":
                subCounter++;
                return String.valueOf(arg1 - arg2);
            case "MUL":
                mulCounter++;
                return String.valueOf(arg1 * arg2);
            case "DIV":
                divCounter++;
                if (arg2 == 0) {
                    System.out.println("Dividing by 0. error.");
                    return "ERROR";
                } else {
                    return String.valueOf(arg1 / arg2);
                }
            default:
                Log.log("Unsupported operation: " + operation);
                return "ERROR";
        }
    }

    private static void reportStats() {
        Log.PREFIX = "STATS";
        boolean isRunning = true;

        while (isRunning) {
            try {
                Thread.sleep(10000);
                System.out.println("Current statistics report:\n" + "Clients connected: " + clientCounter + "\n" + "Number of operations: " + operationsCounter + "\n" + "ADD: " + addCounter + "\n" + "SUB: " + subCounter + "\n" + "MUL: " + mulCounter + "\n" + "DIV: " + divCounter + "\n" + "Errors: " + errorCounter + "\n" + "Sum of results: " + sumOfResults);
            } catch (InterruptedException e) {
                Log.log("Error during statistics report: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Log {
    private static final String LOG_FILE = "log.txt";
    public static String PREFIX = "";

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