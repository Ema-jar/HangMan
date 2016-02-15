import java.io.IOException;

/**
 * Created by Emanuele on 12/02/16.
 */
public class Main {
    public static void main(String [] args) {

        String serverName = "";
        int port = 0;
        Thread t = null;

        //Se ci sono argomenti vuol dire che specifico una porta e un hostname, altrimenti uso quelli di default
        if(args.length >= 2){
            serverName = args[0];
            port = Integer.parseInt(args[1]);
        } else{
            serverName = "localhost";
            port = 6666;
            t = new EchoServer(port);
            t.start();
        }


        EchoClient client = new EchoClient(serverName, port);
        client.startClient();

    }
}
