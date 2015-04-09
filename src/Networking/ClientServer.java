package Networking;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientServer implements Runnable {
    private HostServer observer;
    private Socket clientSocket;
    private DataOutputStream outStream;
    private DataInputStream inStream;
    private Integer HEARTBEATINT = 1500;
    private long lastHBeat = 0;
    private int beatsSent = 0;
    private boolean closeSent = false;
    private volatile boolean running = true;
    public enum COMMANDS {CHAT, HBEATS, HBEATR, JOINROOM, PLAYCHNG, CLOSE}
    private ArrayList<String> dataBuffer = new ArrayList<String>();


    public ClientServer(Socket client, HostServer hostServer){
        this.observer = hostServer;
        this.clientSocket = client;
        try{
            clientSocket.setKeepAlive(true);
        } catch (SocketException se){
            System.err.println(se.toString());
        }
        try {
            outStream = new DataOutputStream(clientSocket.getOutputStream());
            inStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
    }

    @Override
    public void run() {
        lastHBeat = System.currentTimeMillis();
        while (running) {
            if (clientSocket != null) {
                if (((System.currentTimeMillis() - lastHBeat) > HEARTBEATINT*(1+beatsSent))){
                    addBufferData(COMMANDS.HBEATS, ((Integer) dataBuffer.size()).toString());
                    beatsSent++;
                }
                if (needToSend()) {
                    sendData();
                }
                try {
                    if (inStream.available() != 0) {
                        readData();
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe.toString());
                }
                if ((System.currentTimeMillis() - lastHBeat) > HEARTBEATINT*3){
                    //too many failed heartbeats, connection is lost
                    if(!closeSent) {
                        closeSent = true;
                        observer.serverUpdate(COMMANDS.CLOSE, "CONTIMEOUT", this);
                    }
                }
            }
        }
    }

    public void terminate() {
        try{
            clientSocket.close();
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }

        running = false;
    }

    private boolean needToSend(){
        synchronized (dataBuffer) {
            return !dataBuffer.isEmpty();
        }
    }

    private boolean sendData(){
        String message;
        synchronized (dataBuffer){
            message = dataBuffer.get(0);
            dataBuffer.remove(0);
        }
        try {
            outStream.writeUTF(message);
            return true;
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
        return false;
    }

    public void addBufferData(COMMANDS command, String data){
        if((command != null) && (data != null)){
            if(COMMANDS.CHAT == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add("CHAT^" + data);
                }
            } else if(COMMANDS.HBEATS == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add("HBEATS^" + data);
                }
            } else if(COMMANDS.HBEATR == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add(0, "HBEATR^" + data); // add to the front
                }
            } else if(COMMANDS.PLAYCHNG == command){
                synchronized (dataBuffer){
                    dataBuffer.add(0, "PLAYCHNG^" + data);
                }
            } else if(COMMANDS.CLOSE == command){
                synchronized (dataBuffer){
                    dataBuffer.add(0, "CLOSE^" + data);
                }
            }
        }
    }

    public void update(String data) {
        String peelCommand = data.substring(0, data.indexOf("^"));
        data = data.substring(data.indexOf("^") + 1, data.length());
        if (peelCommand.equals("CHAT")) {
            observer.serverUpdate(COMMANDS.CHAT, data, this);
        } else if (peelCommand.equals("HBEATS")) {
            addBufferData(COMMANDS.HBEATR, ((Integer) dataBuffer.size()).toString());
        } else if (peelCommand.equals("HBEATR")) {
            beatsSent = 0;
            lastHBeat = System.currentTimeMillis();
            observer.serverUpdate(COMMANDS.HBEATR, data, this);
        } else if (peelCommand.equals("JOINROOM")){
            observer.serverUpdate(COMMANDS.JOINROOM, data, this);
        } else if (peelCommand.equals("CLOSE")){
            observer.serverUpdate(COMMANDS.CLOSE, data, this);
        }
    }

    public void readData(){
        try {
            String data = inStream.readUTF();
            update(data);
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
