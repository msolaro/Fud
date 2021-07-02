import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author rlspick
 */
public class Client {

    private String name;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String serverResponse = "";

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client() {
    }

    private void start() throws IOException {

        Scanner in = new Scanner(System.in);
        Socket socket = new Socket("localhost", 23100);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        System.out.print("Enter your name: ");
        this.name = in.next();

        outputStream.writeUTF(this.name);

        serverResponse = "";
        while (!serverResponse.equals("You are connected.")) {
            System.out.print("Enter access code: ");
            outputStream.writeUTF(in.next());

            serverResponse = inputStream.readUTF();
            System.out.println("Server Response: " + serverResponse);
        }

        System.out.println("Type /e at any time to EXIT the chat.");
        while (in.hasNextLine()) {
            String message = in.nextLine();

            if (message.equals("/e")) {
                System.out.println(this.name + ": Exiting Chat...");
                break;
            }

            outputStream.writeUTF(message);
            String nameFromServer = inputStream.readUTF();
            String messageFromServer = inputStream.readUTF();
            if (!name.equals(nameFromServer)) {
                System.out.println(nameFromServer + ": " + messageFromServer);
            }
            System.out.print("Message: ");
        }
    }
}
