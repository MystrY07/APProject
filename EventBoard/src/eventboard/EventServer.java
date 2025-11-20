package eventboard;

/**
 *
 * @author aorpr
 */
import java.io.*;
import java.net.*;

public class EventServer {

    private static final int PORT = 6767; //port num
    
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(6767)){
            System.out.println("eventboard now active through port 6767");

            //Acceot clients 
            while (true){
                Socket ServerSideSocket = s.accept();//Socket
                System.out.println("Client side connected to: " + ServerSideSocket.getInetAddress());
                
                //New thread for each client that connects
                Thread t = new Thread (new ClientHandler(ServerSideSocket));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("client side error: " + e.getMessage());
        }
    }
}
