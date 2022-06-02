import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream inputObject;
    private ObjectOutputStream outputObject;
    public Client(String host, int port) {

        try {
            socket = new Socket(host, port);
            outputObject = new ObjectOutputStream(socket.getOutputStream());
            inputObject = new ObjectInputStream(socket.getInputStream());

            Scanner in = new Scanner(System.in);
            while (socket.isConnected()) {
                System.out.print("Input command: (s)end / (r)eceive / (e)xit => ");
                String com = in.nextLine();
                if (com.equalsIgnoreCase("e") || com.equalsIgnoreCase("exit")) {
                    outputObject.writeObject(new ServiceMessage(MessageType.CLOSE_CONNECTION));
                    closeConnection();
                    break;
                }
                if (com.equalsIgnoreCase("s") || com.equalsIgnoreCase("r")) {
                    System.out.println("Input name file");
                    String name = in.nextLine();
                    if (com.equalsIgnoreCase("s")) {
                        String DIR = "../FileTransferIO/ClientIO/src/main/resources/";
                        File fileToSend = new File(DIR + name);
                        if (fileToSend.exists()) {
                            outputObject.writeObject(new ServiceMessage(MessageType.CLIENT_SEND, name, DIR, (int)fileToSend.length()));
                            outputObject.flush();
                            new Thread(() -> sendFile(fileToSend)).start();
                        }
                        else System.out.println("File not found!");
                        Thread.sleep(20);
                    }
                    if (com.equalsIgnoreCase("r")) {
//                        outputData.writeUTF("client_receive_file");
//                        new Thread(() -> receiveFile(name)).start();
//                        Thread.sleep(20);
                    }
                }
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(File file) {
        try (DataInputStream readData = new DataInputStream(Files.newInputStream(file.toPath()))) {
            //Read
            int fileSize = (int)file.length();
            byte [] arrayData = new byte[fileSize];
            readData.readFully(arrayData, 0, fileSize);
            //Send
            outputObject.write(arrayData, 0, fileSize);
            outputObject.flush();
            System.out.println("File transfer completed, transferred " + fileSize + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFile(String file) {

    }

    private void closeConnection() {
        if (!socket.isClosed()) {
            try {
                inputObject.close();
                outputObject.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
                if (socket.isClosed()) System.out.println("Socket close, client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
