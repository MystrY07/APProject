
package eventboard;

/**
 *
 * @author aorpr
 */
import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;

public class EventServer {
    // port number
    private static final int PORT = 6767;
    
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(6767)){
            System.out.println("eventboard now active through port 6767");

            while (true){
                Socket ServerSideSocket = s.accept();
                System.out.println("Client side connected to: " + ServerSideSocket.getInetAddress());
                
                Thread t = new Thread (new ClientHandler(ServerSideSocket));
                t.start();
            
            }
            
        } catch (IOException e) {
            System.out.println("client side error: " + e.getMessage());
        }
    }
    
}
