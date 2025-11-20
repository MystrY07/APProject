package eventboardclient;

/**
 *
 * @author aorpr
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class EventImporterClient {

    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 6767;

        try (Socket socket = new Socket(serverHost, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Event Server at " + serverHost + ":" + serverPort);
            System.out.println("------------Commands-------------");
            System.out.println("-- add; date; time; description");
            System.out.println("-- remove; date; time; description");
            System.out.println("-- list; date; -; -");
            System.out.println("-- import; <public-URL-to-events.txt>");
            System.out.println("-- STOP");
            System.out.println("---------------------------------");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine().trim();

                if (message.equalsIgnoreCase("STOP")) {
                    out.println("STOP");
                    String response = in.readLine();
                    System.out.println("< " + (response != null ? response : "Server has been disconnected"));
                    break;
                }

                // IMPORT command
                if (message.toLowerCase().startsWith("import;")) {
                    String urlString = message.substring(7).trim();
                    performImport(urlString, out, in);
                    continue;
                }

                // Normal command
                out.println(message);
                String response = in.readLine();
                System.out.println("< " + (response != null ? response : "Server disconnected"));
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }


    //Import by URL function
    private static void performImport(String urlString, PrintWriter out, BufferedReader in) {
        int imported = 0;
        int skipped = 0;

        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // Expected format for importing events: date; time; description
                    String[] parts = line.split(";", 3);
                    //if line doesnt have atleast 2 semicolons
                    if (parts.length != 3 ||
                            parts[0].trim().isEmpty() ||
                            parts[1].trim().isEmpty() ||
                            parts[2].trim().isEmpty()) {

                        System.out.println("Skipped Improper formatted line: \"" + line + "\"");
                        skipped++;
                        continue;
                    }

                    String date = parts[0].trim();
                    String time = parts[1].trim();
                    String desc = parts[2].trim();

                    // Construct proper ADD command with proper format
                    String addCmd = String.format("add; %s; %s; %s", date, time, desc);
                    out.println(addCmd);

                    String response = in.readLine();
                    if (response == null) {
                        System.out.println("Server disconnected during import.");
                        break;
                    }

                    System.out.println("< " + response);

                    // If server reports an invalid command, count as skipped
                    if (response.startsWith("InvalidCommandException")) {
                        skipped++;
                    } else {
                        imported++;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to fetch file: " + e.getMessage());
            return;
        }

        System.out.println("Imported: " + imported + "; Skipped: " + skipped);
    }
}
