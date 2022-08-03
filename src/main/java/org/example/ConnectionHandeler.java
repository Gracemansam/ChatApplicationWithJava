package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandeler implements Runnable{
    public static ArrayList<ConnectionHandeler> connectionHandelers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String UserNameOfClient;

    public String getUserNameOfClient() {
        return UserNameOfClient;
    }

    public ConnectionHandeler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.UserNameOfClient = bufferedReader.readLine();
            connectionHandelers.add(this);
            broadcastMessage( UserNameOfClient + ": has entered the chat" );
        }catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
       String messageFromClient ;
       while(socket.isConnected()){
           try{

               messageFromClient = bufferedReader.readLine();
               if (messageFromClient == null) throw new IOException();

               broadcastMessage(messageFromClient);
           }catch (IOException e) {
               closeEverything(socket, bufferedReader, bufferedWriter);
               break;
           }
       }
    }
    public void broadcastMessage(String messageToSend) {
        for (ConnectionHandeler connectionHandeler : connectionHandelers) {
            try {
                if (!connectionHandeler.UserNameOfClient.equals((UserNameOfClient))) {

                    connectionHandeler.bufferedWriter.write(messageToSend);
                    connectionHandeler.bufferedWriter.newLine();
                    connectionHandeler.bufferedWriter.flush();

                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
        public void removeClientHandler(){
//        if(clientUsername == null){
            connectionHandelers.remove(this);
            broadcastMessage( UserNameOfClient + ": has left the chat");
        }


      public void closeEverything(Socket socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(socket != null){
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }catch(IOException e)
        {

            e.printStackTrace();
        }
      }


    }

