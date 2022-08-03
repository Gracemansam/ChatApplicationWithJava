package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandeler> Clients = new ArrayList<>();
    private  Socket socket;
    private ConnectionHandeler connectionHandeler;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
    try {
        while(!serverSocket.isClosed()){
            socket = serverSocket.accept();
            ConnectionHandeler connectionHandeler = new ConnectionHandeler(socket);
            Clients.add(connectionHandeler);
            System.out.println(connectionHandeler.getUserNameOfClient() + " has connected");


          Thread thread = new Thread(connectionHandeler);
          thread.start();

        }


    }catch (Exception e) {

    }finally {
        closeServerSocket();
    }

    }
    public void closeServerSocket()
    {
        try{
            if(serverSocket == null){


                System.out.println("Closed");
                serverSocket.close();


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7768);
        Server server = new Server(serverSocket);
        server.startServer();   //call the startServer

    }


}
