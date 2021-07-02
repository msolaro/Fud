import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rlspick
 */
public class Server {

    private Socket client;
    ServerSocket serverSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private HashSet<String> clients = new HashSet<>();
    private ExecutorService pool = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {
        System.out.println("The chat server is starting...");
        Server server = new Server();

        try {
            server.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {

        serverSocket = new ServerSocket(23100);

        while (true) {
            client = serverSocket.accept();
            Handler runnable = new Handler();
            pool.execute(runnable);
        }
    }

    public class Handler implements Runnable {

        @Override
        public void run() {
            try {

                inputStream = new DataInputStream(client.getInputStream());
                outputStream = new DataOutputStream(client.getOutputStream());

                String name = inputStream.readUTF();
                System.out.println("Chat Name: " + name);

                String code = "";
                while (!code.equals("cs309spring2020")) {
                    code = inputStream.readUTF();
                    System.out.println("User Entered Access Code: " + code);

                    if (code.equals("cs309spring2020")) {
                        outputStream.writeUTF("You are connected.");

                        if (!clients.contains(name)) {
                            clients.add(name);
                        }

                    } else {
                        outputStream.writeUTF("Incorrect Access Code");
                        System.out.println("Incorrect Access Code, Trying Again...");
                    }
                }

                while (true) {

                    String message = inputStream.readUTF();
                    if (message.equals("")) {
                        System.out.print("");
                    } else System.out.println("Message From " + name + ": " + message);

                    outputStream.writeUTF(name);
                    outputStream.writeUTF(message);
                }

            } catch (SocketException e) {
                System.out.println("\nA User Has Left The Chat...\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


