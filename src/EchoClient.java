// File Name GreetingClient.java

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClient {

    String clientName;
    int port;

    public EchoClient(String clientName, int port) {
        this.clientName = clientName;
        this.port = port;
    }

    public void startClient() {

        Scanner reader = new Scanner(System.in);
        String textToSend = "";
        Socket s = null;


        while(true){
            try {
                s = createClientSocket(clientName, port);

                if(null == s){
                    System.out.println("Connection error");
                    s.close();
                    return;
                }

                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                textToSend = reader.next();
                dout.writeUTF(textToSend);
                s.close();

            }catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Socket createClientSocket(String clientName, int port){

        boolean scanning = true;
        Socket socket = null;
        int numberOfTry = 0;

        while (scanning && numberOfTry < 10){
            numberOfTry++;
            try {
                socket = new Socket(clientName, port);
                scanning = false;
            } catch (IOException e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

        }
        return socket;
    }

}