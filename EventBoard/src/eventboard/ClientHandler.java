package eventboard;

/**
 *
 * @author aorpr
 */
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        try (
            //Read from client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //Write to client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            
            String inputLine;
            //main comm loop
            while ((inputLine = in.readLine()) != null) {
                //STOP
                if (inputLine.equalsIgnoreCase("STOP")) {
                    out.println("TERMINATE");
                    break;
                }
                // checks for correct expected input
                String[] parts = inputLine.split(";", 4);
                if (parts.length < 4) {
                    out.println("ERROR: Invalid message format");
                    continue;
                }

                String action = parts[0].trim().toLowerCase();
                String date = parts[1].trim();
                String time = parts[2].trim();
                String desc = parts[3].trim();

                try {
                    //switch cases for each action
                    switch (action) {
                        case "add":
                            out.println(EventActions.add(date, time, desc));
                            break;
                        case "remove":
                            out.println(EventActions.remove(date, time, desc));
                            break;
                        case "list":
                            out.println(EventActions.list(date));
                            break;
                        default:
                            out.println("ERROR: Unknown action");
                    }
                } catch (InvalidActionException e) {
                    out.println("ERROR: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
        }
    }
}
