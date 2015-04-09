package Networking;

import MultiplayerSetup.JoinController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientConnection implements Runnable {
    private JoinController observer;
    private Socket clientSocket;
    private DataOutputStream outStream;
    private DataInputStream inStream;
    private Integer HEARTBEATINT = 1500;
    private long lastHBeat = 0;
    private int beatSent = 0;
    private boolean closeSent = false;
    private volatile boolean running = true;
    public enum COMMANDS {CHAT, HBEATS, HBEATR, JOINROOM, PLAYCHNG, CLOSE}
    private ArrayList<String> dataBuffer = new ArrayList<String>();

    public ClientConnection(String IP, Integer port, JoinController joinController){
        this.observer = joinController;
        try{
            if(IP != null && port != null) {
                clientSocket = new Socket(IP, port);
                outStream = new DataOutputStream(clientSocket.getOutputStream());
                inStream = new DataInputStream(clientSocket.getInputStream());
                try{
                    clientSocket.setKeepAlive(true);
                } catch (SocketException se){
                    System.err.println(se.toString());
                }
            }
        } catch (UnknownHostException uhe){
            System.err.println(uhe.toString());
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
    }

    public void terminate() {
        try{
            if(clientSocket != null) {
                if (!clientSocket.isClosed()){
                    clientSocket.close();
                }
            }
        } catch (IOException ioe){
            System.err.println(ioe.toString());
        }
        running = false;
    }


    @Override
    public void run() {
        lastHBeat = System.currentTimeMillis();
        while (running) {
            if (clientSocket != null) {
                if (((System.currentTimeMillis() - lastHBeat) > HEARTBEATINT*(1+beatSent))){
                    addBufferData(COMMANDS.HBEATS, ((Integer) dataBuffer.size()).toString());
                    beatSent++;
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
                    if(!closeSent){
                        closeSent = true;
                        observer.clientUpdate(COMMANDS.CLOSE, "CONTIMEOUT");
                    }
                }
            }
        }
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
        if((command != null) && (data != null)) {
            if (COMMANDS.CHAT == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add("CHAT^" + data);
                }
            } else if (COMMANDS.HBEATS == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add("HBEATS^" + data);
                }
            } else if (COMMANDS.HBEATR == command) {
                synchronized (dataBuffer) {
                    dataBuffer.add(0, "HBEATR^" + data); // add to the front
                }
            } else if (COMMANDS.JOINROOM == command){
                synchronized (dataBuffer) {
                    dataBuffer.add(0, "JOINROOM^" + data); // add to the front
                }
            } else if(COMMANDS.CLOSE == command){
                synchronized (dataBuffer){
                    dataBuffer.add(0, "CLOSE^" + data);
                }
            }
        }
    }

    public void update(String data){
        String peelCommand =  data.substring(0, data.indexOf("^"));
        data = data.substring(data.indexOf("^") + 1, data.length());
        if (peelCommand.equals("CHAT")) {
            observer.clientUpdate(COMMANDS.CHAT, data);
        } else if (peelCommand.equals("HBEATS")){
            addBufferData(COMMANDS.HBEATR, ((Integer) dataBuffer.size()).toString());
        } else if (peelCommand.equals("HBEATR")){
            beatSent = 0;
            lastHBeat = System.currentTimeMillis(); //received heartbeat so update the time
            observer.clientUpdate(COMMANDS.HBEATR, data);
        } else if (peelCommand.equals("PLAYCHNG")) {
            observer.clientUpdate(COMMANDS.PLAYCHNG, data);
        } else if (peelCommand.equals("CLOSE")){
            observer.clientUpdate(COMMANDS.CLOSE, data);
        }
    }

    public void readData() {
        try {
            String data = inStream.readUTF();
            update(data);
        } catch (InterruptedIOException iioe){
            System.err.println(iioe.toString());
        } catch (IOException ioe){
            // terminate();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
