package Networking;

import MultiplayerSetup.HostController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostServer implements Runnable {
    private HostController observer;
    private ServerSocket primServer;
    private DataOutputStream outStream;
    private DataInputStream inStream;
    private ArrayList<ClientServer> clientServers = new ArrayList<ClientServer>();
    private ArrayList<Thread> clientThreads = new ArrayList<Thread>();
    private Integer MAXCONS = 0;
    private Integer BACKLOG = 0;
    private volatile boolean running = true;
    private boolean bound = false;

    public HostServer(int port, HostController hostController){
        this.observer = hostController;
        try {
            primServer = new ServerSocket(port, BACKLOG);
            bound = true;
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        }
    }

    @Override
    public void run() {
        while (running){
            while ((clientServers.size() < MAXCONS) && running){
                try{
                    Socket clientSocket = primServer.accept();
                    outStream = new DataOutputStream(clientSocket.getOutputStream());
                    inStream = new DataInputStream(clientSocket.getInputStream());
                    ClientServer clientServer = new ClientServer(clientSocket, this);
                    clientServers.add(clientServer);
                    Thread thread = new Thread(clientServer);
                    clientThreads.add(thread);
                    thread.start();
                } catch (IOException ioe){
                    System.err.println(ioe.toString());
                }
            }
        }
    }

    public void terminate() {
        try {
            int size = clientServers.size();
            for (int i = size - 1; i >= 0; i--) {
                clientServers.get(i).terminate();
                clientServers.remove(clientServers.get(i));
            }
            size = clientThreads.size();
            for (int i = size - 1; i >= 0; i--) {
                clientThreads.get(i).join();
                clientThreads.remove(clientThreads.get(i));
            }
        } catch (InterruptedException ie){
            System.err.println(ie.toString());
        }
        try{
            if(primServer != null) {
                primServer.close();
            }
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
        running = false;
    }

    public void killConnection(ClientServer clientServer, String reason){
        clientServer.addBufferData(ClientServer.COMMANDS.CLOSE, reason);
        int servIndex = -1;
        for( int i = 0; i < clientServers.size(); i++){
            if(clientServers.get(i) == clientServer){
                servIndex = i;
                break;
            }
        }
        if(servIndex != -1){
            try {
                clientServers.get(servIndex).terminate();
                clientServers.remove(clientServers.get(servIndex));
                clientThreads.get(servIndex).join();
                clientThreads.remove(clientThreads.get(servIndex));
            } catch (InterruptedException ie){
                System.err.println(ie.toString());
            }
        }
    }

    public ServerSocket getPrimServer() {
        return primServer;
    }

    public void setMAXCONS(Integer maxcons){
        MAXCONS = maxcons;
    }

    public void addDataToAll(ClientServer.COMMANDS command, String data){
        for(ClientServer clientServer: clientServers){
            clientServer.addBufferData(command, data);
        }
    }

    public ArrayList<ClientServer> getClientServers() {
        return clientServers;
    }

    public void serverUpdate(ClientServer.COMMANDS command, String data, ClientServer sender){
        observer.serverUpdate(command, data, sender);
    }

    public int clientServerIndex(ClientServer clientServer){
        for(int i = 0; i < clientServers.size(); i++){
            if(clientServers.get(i) == clientServer){
                return i;
            }
        }
        return -1;
    }

    public boolean isBound() {
        return bound;
    }
}
